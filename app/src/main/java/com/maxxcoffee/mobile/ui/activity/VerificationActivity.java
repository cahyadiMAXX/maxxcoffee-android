package com.maxxcoffee.mobile.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.controller.ProfileController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.database.entity.ProfileEntity;
import com.maxxcoffee.mobile.model.request.ResendEmailRequestModel;
import com.maxxcoffee.mobile.model.request.VerifySmsCodeRequestModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.ProfileItemResponseModel;
import com.maxxcoffee.mobile.model.response.ProfileResponseModel;
import com.maxxcoffee.mobile.task.profile.ProfileTask;
import com.maxxcoffee.mobile.task.user.ResendEmailTask;
import com.maxxcoffee.mobile.task.user.VerificationCodeTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rioswarawan on 7/16/16.
 */
public class VerificationActivity extends AppCompatActivity {

    @Bind(R.id.layout_email_verified)
    LinearLayout layoutEmailVerified;
    @Bind(R.id.layout_email_not_verified)
    LinearLayout layoutEmailNotVerified;
    @Bind(R.id.email_verified)
    TextView emailVerified;
    @Bind(R.id.email_not_verified)
    TextView emailNotVerified;

    @Bind(R.id.layout_sms_verified)
    LinearLayout layoutSmsVerified;
    @Bind(R.id.layout_sms_not_verified)
    LinearLayout layoutSmsNotVerified;
    @Bind(R.id.phone_verified)
    TextView phoneVerified;
    @Bind(R.id.phone_not_verified)
    TextView phoneNotVerified;
    @Bind(R.id.layout_change_phone)
    LinearLayout layoutChangePhone;
    @Bind(R.id.root_layout)
    LinearLayout rootLayout;
    //@Bind(R.id.verification_code)
    //public EditText verificationCode;

    public static EditText verificationCode;
    private Integer redirectFragment;
    private ProfileController profileController;
    private CardController cardController;
    private int mDayPart = Utils.getDayPart();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        ButterKnife.bind(this);

        verificationCode = (EditText) findViewById(R.id.verification_code);

        redirectFragment = getIntent().getIntExtra("redirect-fragment", -999);
        profileController = new ProfileController(this);
        cardController = new CardController(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackground(mDayPart);

        getLocalProfile();
        fetchingData();
    }

    private void fetchingData() {
        if(!Utils.isConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            return;
        }
        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getSupportFragmentManager(), null);*/
        final Dialog loading;
        loading = new Dialog(VerificationActivity.this);
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);
        loading.show();

        ProfileTask task = new ProfileTask(this) {
            @Override
            public void onSuccess(ProfileResponseModel profile) {
                ProfileItemResponseModel profileItem = profile.getUser_profile().get(0);

                if (profileItem != null) {
                    ProfileEntity profileEntity = new ProfileEntity();
                    profileEntity.setId(profileItem.getId_user());
                    profileEntity.setName(profileItem.getNama_user());
                    profileEntity.setEmail(profileItem.getEmail());
                    profileEntity.setCity(profileItem.getKota_user());
                    profileEntity.setPhone(profileItem.getMobile_phone_user());
                    profileEntity.setBirthday(profileItem.getTanggal_lahir());
                    profileEntity.setGender(profileItem.getGender());
                    profileEntity.setOccupation(profileItem.getOccupation());
                    profileEntity.setImage(profileItem.getGambar());
                    profileEntity.setPoint(Integer.parseInt(profile.getTotal_point()));
                    profileEntity.setBalance(Integer.parseInt(profile.getTotal_balance()));
                    profileEntity.setSms_verified(profileItem.getVerifikasi_sms().equalsIgnoreCase("yes"));
                    profileEntity.setEmail_verified(profileItem.getVerifikasi_email().equalsIgnoreCase("yes"));

                    PreferenceManager.putBool(VerificationActivity.this, Constant.PREFERENCE_SMS_VERIFICATION, profileItem.getVerifikasi_sms().equalsIgnoreCase("yes"));
                    PreferenceManager.putBool(VerificationActivity.this, Constant.PREFERENCE_EMAIL_VERIFICATION, profileItem.getVerifikasi_email().equalsIgnoreCase("yes"));

                    profileController.insert(profileEntity);

                    List<CardItemResponseModel> cards = profile.getCards();
                    if (cards.size() > 0) {
                        for (CardItemResponseModel card : cards) {
                            CardEntity cardEntity = new CardEntity();
                            cardEntity.setId(card.getId_card());
                            cardEntity.setName(card.getCard_name());
                            cardEntity.setNumber(card.getCard_number());
                            cardEntity.setImage(card.getCard_image());
                            cardEntity.setDistribution_id(card.getDistribution_id());
                            cardEntity.setCard_pin(card.getCard_pin());
                            cardEntity.setBalance(card.getBalance());
                            cardEntity.setPoint(card.getBeans());
                            cardEntity.setExpired_date(card.getExpired_date());

                            cardController.insert(cardEntity);
                        }
                    }

                    getLocalProfile();
                }
                PreferenceManager.putString(VerificationActivity.this, Constant.PREFERENCE_BALANCE, String.valueOf(profile.getTotal_balance()));
                PreferenceManager.putString(VerificationActivity.this, Constant.PREFERENCE_BEAN, String.valueOf(profile.getTotal_point()));
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing()){
                    loading.dismiss();
                }
            }

            @Override
            public void onFailed() {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing()){
                    loading.dismiss();
                }
                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    private void setEmailLayout(boolean emailVerified) {
        layoutEmailVerified.setVisibility(emailVerified ? View.VISIBLE : View.GONE);
        layoutEmailNotVerified.setVisibility(emailVerified ? View.GONE : View.VISIBLE);
    }

    private void setSmsLayout(boolean smsVerified) {
        layoutSmsVerified.setVisibility(smsVerified ? View.VISIBLE : View.GONE);
        layoutSmsNotVerified.setVisibility(smsVerified ? View.GONE : View.VISIBLE);
    }

    private void getLocalProfile() {
        ProfileEntity profile = profileController.getProfile();

        if (profile != null) {
            emailVerified.setText(profile.getEmail());
            emailNotVerified.setText(profile.getEmail());
            phoneVerified.setText(" " + profile.getPhone());
            phoneNotVerified.setText(" " + profile.getPhone());

            String verCode = PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_VERIFICATION_CODE, "");
            verificationCode.setText(verCode);

            setSmsLayout(profile.getSms_verified());
            setEmailLayout(profile.getEmail_verified());

            checkIfAllVerified();
        }
    }

    @OnClick(R.id.resend_email)
    public void onResendEmailClick() {
        if(!Utils.isConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            return;
        }
        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getSupportFragmentManager(), null);*/
        final Dialog loading;
        loading = new Dialog(VerificationActivity.this);
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);
        loading.show();

        final ProfileEntity profile = profileController.getProfile();
        if (profile != null) {
            ResendEmailRequestModel body = new ResendEmailRequestModel();
            body.setEmail(profile.getEmail());
            body.setResend_email("yes");

            ResendEmailTask task = new ResendEmailTask(this) {
                @Override
                public void onSuccess() {
                    //progress.dismissAllowingStateLoss();
                    if (loading.isShowing()) loading.dismiss();
                    showDialog("We have sent the verification link to your email.");
                }

                @Override
                public void onWait(String second) {
                    if (loading.isShowing()) loading.dismiss();
                    showDialog("We have sent the verification link to your email. Please wait " + second + " second to retry");
                }

                @Override
                public void onFailed() {
                    if (loading.isShowing()) loading.dismiss();
                    Toast.makeText(VerificationActivity.this, "Failed to resend email verification", Toast.LENGTH_SHORT).show();
                }
            };
            task.execute(body);
        }
    }

    @OnClick(R.id.sync)
    public void onSyncClick() {
        fetchingData();
    }

    @OnClick(R.id.resend_sms)
    public void onResendSmsClick() {
        if(!Utils.isConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            return;
        }
        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getSupportFragmentManager(), null);*/
        final Dialog loading;
        loading = new Dialog(VerificationActivity.this);
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);
        loading.show();

        final ProfileEntity profile = profileController.getProfile();
        if (profile != null) {
            ResendEmailRequestModel body = new ResendEmailRequestModel();
            body.setEmail(profile.getEmail());
            body.setResend_sms("yes");

            ResendEmailTask task = new ResendEmailTask(this) {
                @Override
                public void onSuccess() {
                    //progress.dismissAllowingStateLoss();
                    if (loading.isShowing()) loading.dismiss();
                    showDialog("We have sent the verification code to your mobile phone");
                }

                @Override
                public void onWait(String second) {
                    //progress.dismissAllowingStateLoss();
                    if (loading.isShowing()) loading.dismiss();
                    showDialog("We have sent the verification code to your mobile phone. Please wait " + second + " second to retry");
                }

                @Override
                public void onFailed() {
                    //progress.dismiss();
                    if (loading.isShowing()) loading.dismiss();
                    Toast.makeText(VerificationActivity.this, "Failed to resend verification code", Toast.LENGTH_SHORT).show();
                }
            };
            task.execute(body);
        }
    }

    @OnClick(R.id.layout_change_phone)
    public void onChangePhoneClick() {
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra("content", FormActivity.CHANGE_PHONE);

        startActivity(intent);
    }

    @OnClick(R.id.check_code)
    public void onCheckCOdeClick() {
        if(!Utils.isConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            return;
        }
        if (!isFormValid())
            return;

        final ProfileEntity profile = profileController.getProfile();
        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getSupportFragmentManager(), null);*/
        final Dialog loading;
        loading = new Dialog(VerificationActivity.this);
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);
        loading.show();

        VerifySmsCodeRequestModel body = new VerifySmsCodeRequestModel();
        body.setEmail(profile.getEmail());
        body.setVerify_sms(verificationCode.getText().toString());

        VerificationCodeTask task = new VerificationCodeTask(this) {
            @Override
            public void onSuccess() {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing()) loading.dismiss();
                //verification code dihapus aj klo udah berhasil
                PreferenceManager.putString(getApplicationContext(), Constant.PREFERENCE_VERIFICATION_CODE, "");
                onSyncClick();
            }

            @Override
            public void onFailed() {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing()) loading.dismiss();
                Toast.makeText(VerificationActivity.this, "Failed to verify code", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute(body);
    }

    private boolean isFormValid() {
        if (verificationCode.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(VerificationActivity.this, "Failed to verify code", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showDialog(String content) {
        Bundle bundle = new Bundle();
        bundle.putString("content", content);

        /*OkDialog optionDialog = new OkDialog() {
            @Override
            protected void onOk() {
                dismiss();
            }
        };
        optionDialog.setArguments(bundle);
        optionDialog.show(getSupportFragmentManager(), null);*/
        final LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_ok, null);
        AlertDialog.Builder builder  = new AlertDialog.Builder(VerificationActivity.this).setView(layout);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView cont = (TextView) layout.findViewById(R.id.content);
        Button buttonSerial = (Button) layout.findViewById(R.id.ok);

        cont.setText(content);
        buttonSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void checkIfAllVerified() {

        boolean smsVerified = PreferenceManager.getBool(VerificationActivity.this, Constant.PREFERENCE_SMS_VERIFICATION, false);
        boolean emailVerified = PreferenceManager.getBool(VerificationActivity.this, Constant.PREFERENCE_EMAIL_VERIFICATION, false);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (smsVerified && emailVerified) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    Intent in = new Intent(getApplicationContext(), MainActivity.class);
                    in.putExtra("content", redirectFragment);
                    startActivity(in);
                }
            }, 3000);
        }
    }

    private void setBackground(int dayPart) {
        int bg = R.drawable.bg_morning_navbar;

        if (dayPart == Utils.MORNING) {
            bg = R.drawable.bg_morning_navbar;
        } else if (dayPart == Utils.AFTERNOON) {
            bg = R.drawable.bg_afternoon_navbar;
        } else if (dayPart == Utils.EVENING) {
            bg = R.drawable.bg_evening_navbar;
        }

        if (rootLayout != null) {
            rootLayout.setBackgroundResource(bg);
        }
    }

}

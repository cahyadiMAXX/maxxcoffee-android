package com.maxxcoffee.mobile.ui.profile;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.ui.activity.FormActivity;
import com.maxxcoffee.mobile.ui.activity.MainActivity;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.controller.ProfileController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.database.entity.ProfileEntity;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.ProfileItemResponseModel;
import com.maxxcoffee.mobile.model.response.ProfileResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.maxxcoffee.mobile.task.profile.ProfileTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class ProfileFragment extends Fragment {

    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.city)
    TextView city;
    @Bind(R.id.occupation)
    TextView occupation;
    @Bind(R.id.birthday)
    TextView birthday;
    @Bind(R.id.gender)
    TextView gender;
    @Bind(R.id.email)
    TextView email;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.balance)
    TextView balance;
    @Bind(R.id.beans)
    TextView beans;
    @Bind(R.id.card)
    TextView card;
    @Bind(R.id.version)
    TextView version;
    @Bind(R.id.referral)
    TextView referral;
    @Bind(R.id.arrow_share)
    ImageView arrow_share;

    private MainActivity activity;
    private ProfileController profileController;
    private CardController cardController;

    public ProfileFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);
        profileController = new ProfileController(activity);
        cardController = new CardController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Profile");

        arrow_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "My Maxx referral code: " + referral.getText().toString();
                //share("facebook", shareBody);
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_using)));
            }
        });

        return view;
    }

    public void share(String nameApp, String message) {
        try {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("image/jpeg");
            List<ResolveInfo> resInfo = getActivity().getPackageManager()
                    .queryIntentActivities(share, 0);
            if (!resInfo.isEmpty()) {
                for (ResolveInfo info : resInfo) {
                    Intent targetedShare = new Intent(
                            android.content.Intent.ACTION_SEND);
                    targetedShare.setType("image/jpeg"); // put here your mime
                    // type
                    if (info.activityInfo.packageName.toLowerCase().contains(
                            nameApp)
                            || info.activityInfo.name.toLowerCase().contains(
                            nameApp)) {
                        targetedShare.putExtra(Intent.EXTRA_SUBJECT,
                                "Sample Photo");
                        targetedShare.putExtra(Intent.EXTRA_TEXT, message);
                        targetedShare.setPackage(info.activityInfo.packageName);
                        targetedShareIntents.add(targetedShare);
                    }
                }
                Intent chooserIntent = Intent.createChooser(
                        targetedShareIntents.remove(0), "Select app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                        targetedShareIntents.toArray(new Parcelable[] {}));
                startActivity(chooserIntent);
            }
        } catch (Exception e) {
            Log.v("VM",
                    "Exception while sending image on" + nameApp + " "
                            + e.getMessage());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        boolean logoutNow = PreferenceManager.getBool(activity, Constant.PREFERENCE_ROUTE_TO_LOGOUT, false);
        if (logoutNow) {
            activity.logoutNow();
        } else {
            getLocalProfile();
            fetchingData();
        }
    }

    private void getLocalProfile() {
        ProfileEntity profile = profileController.getProfile();
        List<CardEntity> cards = cardController.getCards();
        if (profile != null) {
            try {
                DateFormat mDateFormat = new SimpleDateFormat(Constant.DATEFORMAT_STRING_2);
                Date birth = new SimpleDateFormat(Constant.DATEFORMAT_STRING_SIMPLE).parse(profile.getBirthday());

                name.setText(profile.getName());
                city.setText(profile.getCity());
                occupation.setText(profile.getOccupation());
                gender.setText(profile.getGender());
                birthday.setText(mDateFormat.format(birth));
                email.setText(profile.getEmail());
                phone.setText(profile.getPhone());
                balance.setText("IDR " + String.valueOf(profile.getBalance()));
                beans.setText(String.valueOf(profile.getPoint()));
                card.setText(String.valueOf(cards.size()));
                referral.setText(profile.getUser_code());

                PreferenceManager.putString(activity, Constant.PREFERENCE_BALANCE, String.valueOf(profile.getBalance()));
                PreferenceManager.putString(activity, Constant.PREFERENCE_BEAN, String.valueOf(profile.getPoint()));
                //
                PreferenceManager.putString(activity, Constant.PREFERENCE_FIRST_NAME, profile.getFirst_name());
                PreferenceManager.putString(activity, Constant.PREFERENCE_LAST_NAME, profile.getLast_name());
                PreferenceManager.putString(activity, Constant.PREFERENCE_PROFILE_OCCUPATION, profile.getOccupation());
                PreferenceManager.putString(activity, Constant.PREFERENCE_PROFILE_CITY, profile.getCity());
                PreferenceManager.putString(activity, Constant.PREFERENCE_PROFILE_REFERRAL, profile.getUser_code());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        try{
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            String ver = pInfo.versionName;
            version.setText(String.valueOf(ver));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void fetchingData() {
        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);*/
        final Dialog loading;
        loading = new Dialog(getActivity());
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);
        loading.show();

        ProfileTask task = new ProfileTask(activity) {
            @Override
            public void onSuccess(ProfileResponseModel profile) {
                //Log.d("ProfileResponseModel", profile.toString());
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
                    profileEntity.setFirst_name(profileItem.getFirst_name());
                    profileEntity.setLast_name(profileItem.getLast_name());
                    profileEntity.setUser_code(profileItem.getUser_code());

                    PreferenceManager.putBool(activity, Constant.PREFERENCE_SMS_VERIFICATION, profileItem.getVerifikasi_sms().equalsIgnoreCase("yes"));
                    PreferenceManager.putBool(activity, Constant.PREFERENCE_EMAIL_VERIFICATION, profileItem.getVerifikasi_email().equalsIgnoreCase("yes"));
                    //
                    PreferenceManager.putString(activity, Constant.PREFERENCE_FIRST_NAME, profileItem.getFirst_name());
                    PreferenceManager.putString(activity, Constant.PREFERENCE_LAST_NAME, profileItem.getLast_name());
                    PreferenceManager.putString(activity, Constant.PREFERENCE_PROFILE_OCCUPATION, profileItem.getOccupation());
                    PreferenceManager.putString(activity, Constant.PREFERENCE_PROFILE_CITY, profileItem.getKota_user());
                    PreferenceManager.putString(activity, Constant.PREFERENCE_PROFILE_REFERRAL, profileItem.getUser_code());
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
                PreferenceManager.putString(activity, Constant.PREFERENCE_BALANCE, String.valueOf(profile.getTotal_balance()));
                PreferenceManager.putString(activity, Constant.PREFERENCE_BEAN, String.valueOf(profile.getTotal_point()));
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
            }

            @Override
            public void onFailed() {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
                Toast.makeText(activity, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    @OnClick(R.id.layout_name)
    public void onNameClick() {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra("content", FormActivity.CHANGE_NAME);
        startActivity(intent);
    }

    @OnClick(R.id.layout_email)
    public void onEmailClick() {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra("content", FormActivity.CHANGE_EMAIL);
        startActivity(intent);
    }

    @OnClick(R.id.layout_change_password)
    public void onChangePasswordClick() {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra("content", FormActivity.CHANGE_PASSWORD);
        startActivity(intent);
    }

    @OnClick(R.id.layout_occupation)
    public void onChangeOccupationClick() {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra("content", FormActivity.CHANGE_OCCUPATION);
        startActivity(intent);
    }

    @OnClick(R.id.layout_city)
    public void onCityClick() {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra("content", FormActivity.CHANGE_CITY);
        startActivity(intent);
    }
}

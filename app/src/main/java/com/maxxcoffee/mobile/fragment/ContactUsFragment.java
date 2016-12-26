package com.maxxcoffee.mobile.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.controller.ProfileController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.database.entity.ProfileEntity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.fragment.dialog.ReportDialog;
import com.maxxcoffee.mobile.model.CardModel;
import com.maxxcoffee.mobile.model.ReportModel;
import com.maxxcoffee.mobile.model.request.ContactUsRequestModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.ProfileItemResponseModel;
import com.maxxcoffee.mobile.model.response.ProfileResponseModel;
import com.maxxcoffee.mobile.task.ContactUsTask;
import com.maxxcoffee.mobile.task.ProfileTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class ContactUsFragment extends Fragment {

    private final int COMPLAINT = 999;
    private final int QUESTION = 888;
    private final int PARTNERSHIP = 777;

    @Bind(R.id.subject)
    TextView subject;
    @Bind(R.id.detail)
    TextView detail;

    private MainActivity activity;
    private Integer selectedReport;
    private Integer selectedCard;
    private List<ReportModel> data;
    private List<CardModel> cards;
    private ProfileController profileController;
    private CardController cardController;

    public ContactUsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);

        data = new ArrayList<>();
        cards = new ArrayList<>();
        profileController = new ProfileController(activity);
        cardController = new CardController(activity);
        //fetchingData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Contact Us");

        fetchingSubject();
        selectedReport = COMPLAINT;

        return view;
    }

    @OnClick(R.id.subject)
    public void onSubjectClick() {
        ReportDialog reportDialog = new ReportDialog() {
            @Override
            protected void onOk(Integer selectedReport) {
                try {
                    if (selectedReport == COMPLAINT) {
                        setReport(data.get(0));
                    } else if (selectedReport == QUESTION) {
                        setReport(data.get(1));
                    } else if (selectedReport == PARTNERSHIP) {
                        setReport(data.get(2));
                    }
                }catch (Exception e){e.printStackTrace();}
                dismiss();
            }

            @Override
            protected void onCancel() {
                dismiss();
            }
        };

        Bundle bundle = new Bundle();
        bundle.putInt("selected-report", selectedReport);

        reportDialog.setArguments(bundle);
        reportDialog.show(getFragmentManager(), null);
    }

    @OnClick(R.id.arrow_subject)
    public void onSubjectArrowClick() {
        onSubjectClick();
    }

    @OnClick(R.id.report)
    public void onReportClick() {
        reportComplaintNow();
    }

    private void reportComplaintNow() {
        if(!Utils.isConnected(activity)){
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            return;
        }

        if (!isFormValid())
            return;


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

        ContactUsRequestModel body = new ContactUsRequestModel();
        body.setEmail(PreferenceManager.getString(activity, Constant.PREFERENCE_EMAIL, ""));
        body.setNama(PreferenceManager.getString(activity, Constant.PREFERENCE_USER_NAME, ""));
        body.setNohp(PreferenceManager.getString(activity, Constant.PREFERENCE_PHONE, ""));
        body.setPerihal(subject.getText().toString());
        body.setPesan(detail.getText().toString());

        ContactUsTask task = new ContactUsTask(activity) {
            @Override
            public void onSuccess() {
                if (loading.isShowing())loading.dismiss();
                //progress.dismissAllowingStateLoss();
                Toast.makeText(activity, "Your message has been submitted successfully", Toast.LENGTH_SHORT).show();

                selectedReport = -1;
                subject.setText("");
                detail.setText("");
            }

            @Override
            public void onFailed() {
                if (loading.isShowing())loading.dismiss();
                //progress.dismissAllowingStateLoss();
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        };
        task.execute(body);
    }

    private boolean isFormValid() {
        String mDetail = detail.getText().toString();
        String mPerihal = subject.getText().toString();

        if (mPerihal.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Subject cannot empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mDetail.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Detail cannot empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setReport(ReportModel model) {
        selectedReport = model.getId();
        subject.setText(model.getName());
    }

    //ga fetch data lagi
    //soalnya sudah set email dan phone di awal yes (ok)
    private void fetchingData() {
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        ProfileTask task = new ProfileTask(activity) {
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

                    PreferenceManager.putBool(activity, Constant.PREFERENCE_SMS_VERIFICATION, profileItem.getVerifikasi_sms().equalsIgnoreCase("yes"));
                    PreferenceManager.putBool(activity, Constant.PREFERENCE_EMAIL_VERIFICATION, profileItem.getVerifikasi_email().equalsIgnoreCase("yes"));
                    PreferenceManager.putString(activity, Constant.PREFERENCE_PHONE, profileItem.getMobile_phone_user());

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

                }

                PreferenceManager.putString(activity, Constant.PREFERENCE_BALANCE, String.valueOf(profile.getTotal_balance()));
                PreferenceManager.putString(activity, Constant.PREFERENCE_BEAN, String.valueOf(profile.getTotal_point()));
                fetchingSubject();

                progress.dismissAllowingStateLoss();
            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    private void fetchingSubject() {
        ReportModel modelComplaint = new ReportModel();
        modelComplaint.setId(COMPLAINT);
        modelComplaint.setName("Complaint");
        data.add(modelComplaint);

        ReportModel modelQuestion = new ReportModel();
        modelQuestion.setId(QUESTION);
        modelQuestion.setName("Question");
        data.add(modelQuestion);

        ReportModel modelPartnership = new ReportModel();
        modelPartnership.setId(PARTNERSHIP);
        modelPartnership.setName("Partnership");
        data.add(modelPartnership);
    }
}

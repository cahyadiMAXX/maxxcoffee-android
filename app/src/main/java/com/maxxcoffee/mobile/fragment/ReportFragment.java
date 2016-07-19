package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.fragment.dialog.ReportDialog;
import com.maxxcoffee.mobile.model.CardModel;
import com.maxxcoffee.mobile.model.ReportModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class ReportFragment extends Fragment {

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
//    private CardController cardController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);

        data = new ArrayList<>();
        cards = new ArrayList<>();
//        cardController = new CardController(activity);
        fetchingData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Contact Us");

        selectedReport = COMPLAINT;

        return view;
    }

    @OnClick(R.id.subject)
    public void onSubjectClick() {
        ReportDialog reportDialog = new ReportDialog() {
            @Override
            protected void onOk(Integer selectedReport) {
                if (selectedReport == COMPLAINT) {
                    setReport(data.get(0));
                } else if (selectedReport == QUESTION) {
                    setReport(data.get(1));
                } else if (selectedReport == PARTNERSHIP) {
                    setReport(data.get(2));
                }
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

    }

    private void reportLostCardNow() {

    }

    private void setReport(ReportModel model) {
        selectedReport = model.getId();
        subject.setText(model.getName());
    }

    private void fetchingData() {
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

//        List<CardEntity> cardEntities = cardController.getCards();
//        for (CardEntity entity : cardEntities) {
//            CardModel model = new CardModel();
//            model.setId(entity.getId());
//            model.setName(entity.getName());
//            model.setBalance(entity.getBalance());
//            model.setBeans(entity.getBeans());
//            model.setExpDate(entity.getExpDate());
//            model.setPoint(entity.getPoint());
//            cards.add(model);
//        }
    }
}

package com.maxxcoffee.mobile.ui.fragment.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 3/13/2016.
 */
public class ReportDialog extends DialogFragment {

    private final int COMPLAINT = 999;
    private final int QUESTION = 888;
    private final int PARTNERSHIP = 777;

    @Bind(R.id.complaint)
    TextView complaint;
    @Bind(R.id.question)
    TextView question;
    @Bind(R.id.partnership)
    TextView partnership;
    @Bind(R.id.ok)
    TextView ok;
    @Bind(R.id.cancel)
    TextView cancel;

    Integer selectedReport;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_report);
        dialog.show();

        ButterKnife.bind(this, dialog);

        selectedReport = getArguments().getInt("selected-report", -999);

        if (selectedReport == COMPLAINT) {
            complaint.setTextColor(getResources().getColor(R.color.green_selected));
        } else if (selectedReport == QUESTION) {
            question.setTextColor(getResources().getColor(R.color.green_selected));
        } else if (selectedReport == PARTNERSHIP) {
            partnership.setTextColor(getResources().getColor(R.color.green_selected));
        }

        ok.setTextColor(Color.RED);
        cancel.setTextColor(Color.BLACK);

        return dialog;
    }

    @OnClick(R.id.complaint)
    public void onComplaintClick() {
        selectedReport = COMPLAINT;
        complaint.setTextColor(getResources().getColor(R.color.green_selected));
        question.setTextColor(getResources().getColor(android.R.color.darker_gray));
        partnership.setTextColor(getResources().getColor(android.R.color.darker_gray));
    }

    @OnClick(R.id.question)
    public void onQuestionClick() {
        selectedReport = QUESTION;
        complaint.setTextColor(getResources().getColor(android.R.color.darker_gray));
        question.setTextColor(getResources().getColor(R.color.green_selected));
        partnership.setTextColor(getResources().getColor(android.R.color.darker_gray));
    }

    @OnClick(R.id.partnership)
    public void onPartnershipClick() {
        selectedReport = PARTNERSHIP;
        complaint.setTextColor(getResources().getColor(android.R.color.darker_gray));
        question.setTextColor(getResources().getColor(android.R.color.darker_gray));
        partnership.setTextColor(getResources().getColor(R.color.green_selected));
    }

    @OnClick(R.id.ok)
    public void onOkClick() {
        onOk(selectedReport);
    }

    @OnClick(R.id.cancel)
    public void onCancelClick() {
        onCancel();
    }

    protected void onOk(Integer selectedReport) {
    }

    protected void onCancel() {
    }
}

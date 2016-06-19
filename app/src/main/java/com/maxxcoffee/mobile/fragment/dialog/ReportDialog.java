package com.maxxcoffee.mobile.fragment.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
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
public abstract class ReportDialog extends DialogFragment {

    public static Integer COMPLAINT = 1;
    public static Integer LOST_CARD = 2;

    @Bind(R.id.complaint)
    TextView complaint;
    @Bind(R.id.lost_card)
    TextView lostCard;
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                complaint.setTextColor(getResources().getColor(R.color.green_selected, null));
            } else {
                complaint.setTextColor(getResources().getColor(R.color.green_selected));
            }
        } else if (selectedReport == LOST_CARD) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                lostCard.setTextColor(getResources().getColor(R.color.green_selected, null));
            } else {
                lostCard.setTextColor(getResources().getColor(R.color.green_selected));
            }
        }

        ok.setTextColor(Color.RED);
        cancel.setTextColor(Color.BLACK);

        return dialog;
    }

    @OnClick(R.id.complaint)
    public void onComplaintClick() {
        selectedReport = COMPLAINT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            complaint.setTextColor(getResources().getColor(R.color.green_selected, null));
            lostCard.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
        } else {
            complaint.setTextColor(getResources().getColor(R.color.green_selected));
            lostCard.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    @OnClick(R.id.lost_card)
    public void onLostCardClick() {
        selectedReport = LOST_CARD;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            complaint.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
            lostCard.setTextColor(getResources().getColor(R.color.green_selected, null));
        } else {
            complaint.setTextColor(getResources().getColor(android.R.color.darker_gray));
            lostCard.setTextColor(getResources().getColor(R.color.green_selected));
        }
    }

    @OnClick(R.id.ok)
    public void onOkClick() {
        onOk(selectedReport);
    }

    @OnClick(R.id.cancel)
    public void onCancelClick() {
        onCancel();
    }

    protected abstract void onOk(Integer selectedReport);

    protected abstract void onCancel();
}

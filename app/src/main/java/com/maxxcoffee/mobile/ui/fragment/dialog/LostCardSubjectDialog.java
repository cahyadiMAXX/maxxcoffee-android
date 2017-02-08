package com.maxxcoffee.mobile.ui.fragment.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.model.CardModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 3/13/2016.
 */
public class LostCardSubjectDialog extends DialogFragment {

    public static Integer LOST = 0;
    public static Integer BROKEN = 1;

    @Bind(R.id.lost_card)
    TextView lost;
    @Bind(R.id.broken_card)
    TextView broken;
    @Bind(R.id.ok)
    TextView ok;
    @Bind(R.id.cancel)
    TextView cancel;

    Integer selectedSubject;
    List<CardModel> data;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_lost_card_subject);
        dialog.show();

        ButterKnife.bind(this, dialog);

        data = new ArrayList<>();
        selectedSubject = getArguments().getInt("selected-subject", -999);

        if (selectedSubject == LOST) {
            lost.setTextColor(getResources().getColor(R.color.green_selected));
        } else if (selectedSubject == BROKEN) {
            broken.setTextColor(getResources().getColor(R.color.green_selected));
        }

        ok.setTextColor(Color.RED);
        cancel.setTextColor(Color.BLACK);

        return dialog;
    }

    @OnClick(R.id.lost_card)
    public void onLostCardClick() {
        selectedSubject = LOST;
        lost.setTextColor(getResources().getColor(R.color.green_selected));
        broken.setTextColor(getResources().getColor(android.R.color.darker_gray));
    }

    @OnClick(R.id.broken_card)
    public void onBrokenClick() {
        selectedSubject = BROKEN;
        lost.setTextColor(getResources().getColor(android.R.color.darker_gray));
        broken.setTextColor(getResources().getColor(R.color.green_selected));
    }

    @OnClick(R.id.ok)
    public void onOkClick() {
        onOk(selectedSubject);
    }

    @OnClick(R.id.cancel)
    public void onCancelClick() {
        onCancel();
    }

    protected void onOk(Integer index) {
    }

    protected void onCancel() {
    }
}

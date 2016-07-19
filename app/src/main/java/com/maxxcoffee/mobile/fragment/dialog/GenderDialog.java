package com.maxxcoffee.mobile.fragment.dialog;

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
public class GenderDialog extends DialogFragment {

    private final int MALE = 999;
    private final int FEMALE = 777;

    @Bind(R.id.male)
    TextView male;
    @Bind(R.id.female)
    TextView female;
    @Bind(R.id.ok)
    TextView ok;
    @Bind(R.id.cancel)
    TextView cancel;

    Integer selectedGender;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_gender);
        dialog.show();

        ButterKnife.bind(this, dialog);

        selectedGender = getArguments().getInt("selected-gender", -999);

        if (selectedGender == MALE) {
            male.setTextColor(getResources().getColor(R.color.green_selected));
        } else if (selectedGender == FEMALE) {
            female.setTextColor(getResources().getColor(R.color.green_selected));
        }

        ok.setTextColor(Color.RED);
        cancel.setTextColor(Color.BLACK);

        return dialog;
    }

    @OnClick(R.id.male)
    public void onComplaintClick() {
        selectedGender = MALE;
        male.setTextColor(getResources().getColor(R.color.green_selected));
        female.setTextColor(getResources().getColor(android.R.color.darker_gray));
    }

    @OnClick(R.id.female)
    public void onQuestionClick() {
        selectedGender = FEMALE;
        male.setTextColor(getResources().getColor(android.R.color.darker_gray));
        female.setTextColor(getResources().getColor(R.color.green_selected));
    }

    @OnClick(R.id.ok)
    public void onOkClick() {
        onOk(selectedGender);
    }

    @OnClick(R.id.cancel)
    public void onCancelClick() {
        onCancel();
    }

    protected void onOk(Integer selectedGender) {
    }

    protected void onCancel() {
    }
}

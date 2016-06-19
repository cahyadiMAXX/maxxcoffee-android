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
public abstract class OptionDialog extends DialogFragment {

    public static String CANCEL = "cancel";
    public static String OK = "ok";

    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.ok)
    TextView ok;
    @Bind(R.id.cancel)
    TextView cancel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_option);
        dialog.show();

        ButterKnife.bind(this, dialog);

        String stringContent = getArguments().getString("content", "");
        String defaultButton = getArguments().getString("default", OK);

        if (defaultButton.equalsIgnoreCase(OK)) {
            ok.setTextColor(Color.RED);
            cancel.setTextColor(Color.BLACK);
        } else if (defaultButton.equalsIgnoreCase(CANCEL)) {
            ok.setTextColor(Color.BLACK);
            cancel.setTextColor(Color.RED);
        }
        content.setText(stringContent);

        return dialog;
    }

    @OnClick(R.id.ok)
    public void onOkClick() {
        onOk();
    }

    @OnClick(R.id.cancel)
    public void onCancelClick() {
        onCancel();
    }

    protected abstract void onOk();

    protected abstract void onCancel();
}

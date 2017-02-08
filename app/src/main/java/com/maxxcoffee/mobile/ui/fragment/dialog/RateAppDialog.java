package com.maxxcoffee.mobile.ui.fragment.dialog;

import android.app.Dialog;
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
public class RateAppDialog extends DialogFragment {

    @Bind(R.id.content)
    TextView content;
    //@Bind(R.id.ok)
    //TextView ok;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rate);
        dialog.show();

        ButterKnife.bind(this, dialog);

        //String stringContent = getArguments().getString("content", "");

        //content.setText(stringContent);

        return dialog;
    }

    @OnClick(R.id.now)
    public void onOkClick() {
        onOk();
    }

    @OnClick(R.id.later)
    public void onLaterClick() {
        onLater();
    }

    @OnClick(R.id.no)
    public void onNoClick() {
        onCancel();
    }

    protected void onOk() {}

    protected void onLater() {}

    protected void onCancel() {}
}

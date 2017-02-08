package com.maxxcoffee.mobile.ui.fragment.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.view.WindowManager;

import com.maxxcoffee.mobile.R;

/**
 * Created by Rio Swarawan on 3/13/2016.
 */
public class LoadingDialog extends DialogFragment {

    private boolean show;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //background transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.show();
        return dialog;
    }
}

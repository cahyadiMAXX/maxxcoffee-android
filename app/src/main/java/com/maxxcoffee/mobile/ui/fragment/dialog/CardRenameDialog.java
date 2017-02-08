package com.maxxcoffee.mobile.ui.fragment.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.maxxcoffee.mobile.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 3/13/2016.
 */
public abstract class CardRenameDialog extends DialogFragment {

    @Bind(R.id.name)
    EditText name;

    private String tempName;

    public CardRenameDialog(String name) {
        this.tempName = name;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setContentView(R.layout.dialog_rename_card);
        dialog.show();

        ButterKnife.bind(this, dialog);

        name.setHint(tempName);
        return dialog;
    }

    @OnClick(R.id.ok)
    public void onOkClick() {
        onRename(name.getText().toString());
    }

    @OnClick(R.id.cancel)
    public void onCancelClick() {
        dismiss();
    }

    protected abstract void onRename(String name);
}

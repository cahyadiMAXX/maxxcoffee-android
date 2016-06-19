package com.maxxcoffee.mobile.fragment.dialog;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 3/13/2016.
 */
public class StoreDialog extends DialogFragment {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.contact)
    TextView contact;
    @Bind(R.id.open)
    TextView open;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_store);
        dialog.show();

        ButterKnife.bind(this, dialog);

        String mTitle = getArguments().getString("title", "");
        String mAddress = getArguments().getString("address", "");
        String mContact = getArguments().getString("contact", "");
        String mOpen = getArguments().getString("open", "");

        title.setText(mTitle);
        address.setText(mAddress);
        contact.setText(mContact);
        open.setText(mOpen);

        return dialog;
    }
}

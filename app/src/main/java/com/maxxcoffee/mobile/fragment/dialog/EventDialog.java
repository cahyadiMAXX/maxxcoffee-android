package com.maxxcoffee.mobile.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxxcoffee.mobile.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 3/13/2016.
 */
public class EventDialog extends DialogFragment {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.store)
    TextView store;
    @Bind(R.id.location)
    TextView location;
    @Bind(R.id.image)
    ImageView imageView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_event);
        dialog.show();

        ButterKnife.bind(this, dialog);

        String mTitle = getArguments().getString("title", "");
        String mDate = getArguments().getString("date", "");
        String mLocation = getArguments().getString("location", "");
        String mStore = getArguments().getString("store", "");
        String mImage = getArguments().getString("image", "");

        title.setText(mTitle);
        date.setText(mDate);
        location.setText(mLocation);
        store.setText(mStore);
        Glide.with(getActivity()).load(mImage).centerCrop().crossFade().into(imageView);

        return dialog;
    }
}

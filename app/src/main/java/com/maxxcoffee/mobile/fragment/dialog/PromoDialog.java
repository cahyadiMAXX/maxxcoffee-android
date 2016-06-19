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
public class PromoDialog extends DialogFragment {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.description)
    TextView description;
    @Bind(R.id.image)
    ImageView imageView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_promo);
        dialog.show();

        ButterKnife.bind(this, dialog);

        String mTitle = getArguments().getString("title", "");
        String mDescription = getArguments().getString("desc", "");
        Integer mImage = getArguments().getInt("image", -99);

        title.setText(mTitle);
        description.setText(mDescription);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            imageView.setImageDrawable(getContext().getResources().getDrawable(mImage, null));
        } else {
            imageView.setImageDrawable(getContext().getResources().getDrawable(mImage));
        }

        return dialog;
    }
}

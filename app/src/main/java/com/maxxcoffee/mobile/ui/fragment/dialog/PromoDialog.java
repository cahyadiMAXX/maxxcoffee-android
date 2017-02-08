package com.maxxcoffee.mobile.ui.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    @Bind(R.id.syarat)
    TextView syarat;
    @Bind(R.id.image)
    ImageView imageView;
    @Bind(R.id.syarat_layout)
    LinearLayout syaratLayout;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_detail_promo);
        dialog.show();

        ButterKnife.bind(this, dialog);

        String mTitle = getArguments().getString("title", "");
        String mDescription = getArguments().getString("desc", "");
        String mSyarat = getArguments().getString("syarat", "");
        String mImage = getArguments().getString("image", "");

        title.setText(mTitle);
        description.setText(Html.fromHtml(mDescription));
        syarat.setText(Html.fromHtml(mSyarat));

        syaratLayout.setVisibility(mSyarat.equals("") ? View.GONE : View.VISIBLE);
        Glide.with(getActivity()).load(mImage).centerCrop().crossFade().into(imageView);

        return dialog;
    }
}

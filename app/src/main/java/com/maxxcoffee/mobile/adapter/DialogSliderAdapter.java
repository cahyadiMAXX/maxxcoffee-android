package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.maxxcoffee.mobile.R;

/**
 * Created by Rio Swarawan on 5/31/2016.
 */
public abstract class DialogSliderAdapter extends BaseSliderView {

    String imageUrl;
    Context context;

    public DialogSliderAdapter(Context context, String imageUrl) {
        super(context);
        this.context = context;
        this.imageUrl = imageUrl;
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.item_slideshow, null);
        ImageView target = (ImageView) v.findViewById(R.id.image);
        LinearLayout next = (LinearLayout) v.findViewById(R.id.next);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView subtitle = (TextView) v.findViewById(R.id.subtitle);

        title.setVisibility(View.GONE);
        subtitle.setVisibility(View.GONE);
        Glide.with(context).load(imageUrl).placeholder(R.drawable.ic_no_image).crossFade().into(target);

        next.setVisibility(View.GONE);
        bindEventAndShow(v, target);
        return v;
    }
}

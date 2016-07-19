package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.maxxcoffee.mobile.R;

/**
 * Created by Rio Swarawan on 5/31/2016.
 */
public abstract class WelcomeAdapter extends BaseSliderView {

    Integer res;
    String title;
    String subtitle;
    boolean isNext;

    public WelcomeAdapter(Context context, Integer res, String title, String subtitle, boolean isNext) {
        super(context);
        this.res = res;
        this.title = title;
        this.subtitle = subtitle;
        this.isNext = isNext;
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.item_slideshow, null);
        ImageView target = (ImageView) v.findViewById(R.id.image);
        LinearLayout next = (LinearLayout) v.findViewById(R.id.next);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView subtitle = (TextView) v.findViewById(R.id.subtitle);

        title.setText(this.title);
        subtitle.setText(this.subtitle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            target.setImageDrawable(getContext().getResources().getDrawable(res, null));
        } else {
            target.setImageDrawable(getContext().getResources().getDrawable(res));
        }

        next.setVisibility(isNext ? View.VISIBLE : View.GONE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNext();
            }
        });

        bindEventAndShow(v, target);
        return v;
    }

    protected abstract void onNext();
}

package com.maxxcoffee.mobile.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.database.controller.EventController;
import com.maxxcoffee.mobile.task.DownloadImageTask;
import com.maxxcoffee.mobile.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class DetailPromoFragment extends Fragment {

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

    private FormActivity activity;
    private EventController eventController;

    public DetailPromoFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
        eventController = new EventController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_promo, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle(getArguments().getString("title", ""));

        title.setVisibility(View.GONE);

        fetchingData();
        return view;
    }

    private void fetchingData() {
        String mTitle = getArguments().getString("title", "");
        String mDescription = getArguments().getString("desc", "");
        String mSyarat = getArguments().getString("syarat", "");
        String mImage = getArguments().getString("image", "");

        title.setText(mTitle);

        description.setText(Html.fromHtml(Html.fromHtml(mDescription).toString()));
        syarat.setText(Html.fromHtml(Html.fromHtml(mSyarat).toString()));

        syaratLayout.setVisibility(mSyarat.equals("") ? View.GONE : View.VISIBLE);

        Glide.with(activity).load(mImage).placeholder(R.drawable.ic_no_image).into(imageView);

        /*DownloadImageTask task = new DownloadImageTask(activity) {
            @Override
            protected void onDownloadError() {
                Glide.with(activity).load("").placeholder(R.drawable.ic_no_image).into(imageView);
            }

            @Override
            protected void onImageDownloaded(Bitmap bitmap) {
                Bitmap resizeImage = Utils.getResizedBitmap(bitmap, 0.98f);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                imageView.setImageDrawable(drawable);
            }
        };
        task.execute(mImage);*/
    }
}

package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.maxxcoffee.mobile.R;

/**
 * Created by Rio Swarawan on 5/31/2016.
 */
public class StoreImageAdapter extends BaseSliderView {

    String img;
    Context context;

    public StoreImageAdapter(Context context, String res) {
        super(context);
        this.context = context;
        this.img = res;
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.item_image_horizontal, null);
        final ImageView target = (ImageView) v.findViewById(R.id.item_image);

        Glide.with(context).load(img).placeholder(R.drawable.ic_no_image).crossFade().into(target);

//        DownloadImageTask task = new DownloadImageTask(getContext()) {
//            @Override
//            protected void onDownloadError() {
//                Glide.with(context).load("").placeholder(R.drawable.ic_no_image).into(target);
//            }
//
//            @Override
//            protected void onImageDownloaded(Bitmap bitmap) {
//                Bitmap resizeImage = Utils.getResizedBitmap(bitmap, 0.95f);
//                Drawable drawable = new BitmapDrawable(context.getResources(), resizeImage);
//                target.setImageDrawable(drawable);
//            }
//        };
//        task.execute(img);

        bindEventAndShow(v, target);
        return v;
    }
}

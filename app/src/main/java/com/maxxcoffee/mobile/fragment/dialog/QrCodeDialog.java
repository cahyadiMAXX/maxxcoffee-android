package com.maxxcoffee.mobile.fragment.dialog;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.task.DownloadImageTask;
import com.maxxcoffee.mobile.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 3/13/2016.
 */
public class QrCodeDialog extends DialogFragment {

    @Bind(R.id.ok)
    TextView ok;
    @Bind(R.id.qr)
    ImageView qr;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_qr_code);
        dialog.show();

        String url = getArguments().getString("qr");

        if (url != null) {
            DownloadImageTask barcodeTask = new DownloadImageTask(getContext()) {
                @Override
                protected void onDownloadError() {
                    Glide.with(getContext()).load("").placeholder(R.drawable.ic_no_image).into(qr);
                }

                @Override
                protected void onImageDownloaded(Bitmap bitmap) {
                    Bitmap resizeImage = Utils.getResizedBitmap(bitmap, 0.95f);
                    Drawable drawable = new BitmapDrawable(getResources(), resizeImage);
                    qr.setImageDrawable(drawable);
                }
            };
            barcodeTask.execute(url);
        }

        ButterKnife.bind(this, dialog);
        ok.setTextColor(Color.RED);

        return dialog;
    }

    @OnClick(R.id.ok)
    public void onOkClick() {
        dismiss();
    }
}

package com.maxxcoffee.mobile.ui.fragment.dialog;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.task.card.DownloadImageTask;
import com.maxxcoffee.mobile.util.Utils;

import java.io.File;
import java.io.FileOutputStream;

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
        final String filename = "qrcode.png";

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

    public boolean fileExistance(String fname){
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File file = new File(extStorageDirectory,fname);
        Log.d("filepathexist", file.getAbsolutePath());
        return file.exists();
    }

    public void saveImageBitmap(Bitmap bmp, String filename){
        Bitmap bitmap = null;
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        FileOutputStream outStream = null;
        //Log.e("bitmap", bitmap.toString());
        File file = new File(extStorageDirectory + "/maxx",filename + ".png");
        if (file.exists()) {
            file.mkdirs();
            file.delete();
            file = new File(extStorageDirectory + "/maxx", filename + ".png");
            Log.e("file exist", "" + file + ",Bitmap= " + filename);
        }
        try {
            // make a new bitmap from your file
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
    }
}

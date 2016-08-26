package com.maxxcoffee.mobile.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;

/**
 * Created by rioswarawan on 8/2/16.
 */
public abstract class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private Context context;

    public DownloadImageTask(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            onDownloadError();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        onImageDownloaded(result);
    }

    protected abstract void onDownloadError();

    protected abstract void onImageDownloaded(Bitmap bitmap);
}

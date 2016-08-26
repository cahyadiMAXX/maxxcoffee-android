package com.maxxcoffee.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by rioswarawan on 8/13/16.
 */
public class SimpleScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }


    @Override
    public void handleResult(Result result) {
        openRenameCardDialog(result.getText());
    }

    private void openRenameCardDialog(String cardNumber) {
        Bundle bundle = new Bundle();
        bundle.putString("card-no", cardNumber);

        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra("content", FormActivity.RENAME_CARD);
        intent.putExtras(bundle);

        finish();
        startActivity(intent);
    }
}

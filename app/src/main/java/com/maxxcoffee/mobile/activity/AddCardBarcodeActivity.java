package com.maxxcoffee.mobile.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.fragment.StoreFragment;
import com.maxxcoffee.mobile.util.PermissionUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rioswarawan on 7/19/16.
 */
public class AddCardBarcodeActivity extends Activity {

    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_card_barcode);

        ButterKnife.bind(this);

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

    @OnClick(R.id.start_scan)
    public void onStartScanClick() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, 2);
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, 2);
            }
        } else {
            finish();
            startActivity(new Intent(this, SimpleScannerActivity.class));
        }
    }

    @OnClick(R.id.manual)
    public void onAddCardManualClick() {
        openRenameCardDialog("-999");
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.back)
    public void onBackClick() {
        onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 2) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                finish();
                startActivity(new Intent(this, SimpleScannerActivity.class));
            } else {
                Toast.makeText(this, "Camera permission is NOT granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

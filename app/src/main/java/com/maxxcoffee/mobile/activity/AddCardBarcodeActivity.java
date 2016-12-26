package com.maxxcoffee.mobile.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.adapter.CardAdapter;
import com.maxxcoffee.mobile.adapter.PrimaryCardAdapter;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.controller.CardPrimaryController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.database.entity.CardPrimaryEntity;
import com.maxxcoffee.mobile.fragment.StoreFragment;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.fragment.dialog.OptionDialog;
import com.maxxcoffee.mobile.model.CardModel;
import com.maxxcoffee.mobile.model.request.MarkVirtualCardRequestModel;
import com.maxxcoffee.mobile.model.request.RegisterCardRequestModel;
import com.maxxcoffee.mobile.model.response.AddVirtualResponseModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.task.CheckCardTaskPermission;
import com.maxxcoffee.mobile.task.GenerateVirtualCardTask;
import com.maxxcoffee.mobile.task.MarksAsVirtualCardTask;
import com.maxxcoffee.mobile.task.RegisterCardTask;
import com.maxxcoffee.mobile.task.RemoveVirtualCardFromBankTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PermissionUtil;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rioswarawan on 7/19/16.
 */
public class AddCardBarcodeActivity extends FragmentActivity {

    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.CAMERA};

    @Bind(R.id.virtual)
    Button addVirtualCard;
    @Bind(R.id.virtualarea)
    LinearLayout virtualarea;

    private CardController cardController;
    private CardPrimaryController cardPrimaryController;
    List<CardEntity> cards;
    List<CardPrimaryEntity> cardPrimaryEntities;

    public static int FLAG_VIRTUAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_card_barcode);

        cardController = new CardController(getApplicationContext());
        cardPrimaryController = new CardPrimaryController(getApplicationContext());

        cards = cardController.getCards();
        cardPrimaryEntities = cardPrimaryController.getCards();

        ButterKnife.bind(this);

        //addVirtualCard.setVisibility(View.GONE);
        virtualarea.setVisibility(View.GONE);

        //checkVirtualCardVisible();
    }

    void checkVirtualCardPermission(){
        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getSupportFragmentManager(), null);*/
        final Dialog loading;
        loading = new Dialog(AddCardBarcodeActivity.this);
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);
        loading.show();

        CheckCardTaskPermission task = new CheckCardTaskPermission(getApplicationContext()) {

            @Override
            public void onSuccess() {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing()) loading.dismiss();
                //addVirtualCard.setVisibility(View.VISIBLE);
                virtualarea.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(AddVirtualResponseModel tos) {
                Toast.makeText(getApplicationContext(), tos.getMessages(), Toast.LENGTH_LONG).show();
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing()) loading.dismiss();
                //addVirtualCard.setVisibility(View.GONE);
                virtualarea.setVisibility(View.GONE);
            }

            @Override
            public void onFailed() {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing()) loading.dismiss();
                //addVirtualCard.setVisibility(View.GONE);
                virtualarea.setVisibility(View.GONE);
            }

            @Override
            public void onFailed(AddVirtualResponseModel tos) {
                Toast.makeText(getApplicationContext(), tos.getMessages(), Toast.LENGTH_LONG).show();
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing()) loading.dismiss();
                //addVirtualCard.setVisibility(View.GONE);
                virtualarea.setVisibility(View.GONE);
            }
        };

        task.execute();
    }

    void checkVirtualCardVisible(){
        /*if(cards.size() == 0 && cardPrimaryEntities.size() == 0 && FLAG_VIRTUAL == 1000){
            addVirtualCard.setVisibility(View.VISIBLE);
        }*/
        int cardAmount = PreferenceManager.getInt(getApplicationContext(), Constant.PREFERENCE_CARD_AMOUNT, 0);
        if(cardAmount == 0){
            //addVirtualCard.setVisibility(View.VISIBLE);
            checkVirtualCardPermission();
        }else{
            //addVirtualCard.setVisibility(View.GONE);
            virtualarea.setVisibility(View.GONE);
        }
    }

    private void openRenameCardDialog(String cardNumber) {
        Bundle bundle = new Bundle();
        bundle.putString("card-no", cardNumber);

        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra("content", FormActivity.RENAME_CARD);
        intent.putExtras(bundle);

        //finish();
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
            //finish();
            startActivity(new Intent(this, SimpleScannerActivity.class));
        }
    }

    @OnClick(R.id.virtual)
    public void onAddVirtualCardClick(){
        //finish();
        Intent in = new Intent(getApplicationContext(), AddVirtualCardActivity.class);
        startActivity(in);
    }

    private void backToOrigin(){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                PreferenceManager.putBool(getApplicationContext(), Constant.PREFERENCE_ROUTE_CARD_SUCCESS, true);
                PreferenceManager.putBool(getApplicationContext(), Constant.PREFERENCE_CARD_IS_LOADING, false);
                SimpleDateFormat df = new SimpleDateFormat(Constant.DATEFORMAT_META);
                Date today = new Date();
                String strToday = df.format(today);
                PreferenceManager.putString(getApplicationContext(), Constant.PREFERENCE_CARD_LAST_UPDATE, strToday);
                finish();
            }
        }, 2000);
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
                //finish();
                startActivity(new Intent(this, SimpleScannerActivity.class));
            } else {
                Toast.makeText(this, "Camera permission is NOT granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkVirtualCardVisible();
    }
}

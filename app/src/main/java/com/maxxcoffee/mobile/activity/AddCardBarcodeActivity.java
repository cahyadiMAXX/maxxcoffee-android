package com.maxxcoffee.mobile.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

    private CardController cardController;
    private CardPrimaryController cardPrimaryController;
    List<CardEntity> cards;
    List<CardPrimaryEntity> cardPrimaryEntities;

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

        addVirtualCard.setVisibility(View.GONE);

        if(cards.size() == 0 && cardPrimaryEntities.size() == 0){
            addVirtualCard.setVisibility(View.VISIBLE);
        }
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

    @OnClick(R.id.virtual)
    public void onAddVirtualCardClick(){
        Bundle bundle = new Bundle();
        bundle.putString("content", "You only have 1 chance to get Virtual Card");
        bundle.putString("default", OptionDialog.CANCEL);

        OptionDialog optionDialog = new OptionDialog() {
            @Override
            protected void onOk() {
                dismiss();
                try {
                    if(Utils.isConnected(getApplicationContext())){
                        getVirtualCard();
                    }else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            protected void onCancel() {
                dismiss();
            }
        };
        optionDialog.setArguments(bundle);
        optionDialog.show(getSupportFragmentManager(), null);
    }

    void getVirtualCard(){
        //4 task
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getSupportFragmentManager(), null);

        final RemoveVirtualCardFromBankTask removeVirtualCardFromBankTask = new RemoveVirtualCardFromBankTask(getApplicationContext()) {
            @Override
            public void onSuccess() {
                progress.dismissAllowingStateLoss();
                Toast.makeText(getApplicationContext(), "Virtual card successfully added", Toast.LENGTH_LONG).show();
                onBackClick();
            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
            }
        };

        final MarksAsVirtualCardTask marksAsVirtualCardTask = new MarksAsVirtualCardTask(getApplicationContext()) {
            @Override
            public void onSuccess() {
                String distributionid = PreferenceManager.getString(getApplicationContext(), Constant.PRERERENCE_CARD_DISTRIBUTION_ID, "");
                if(distributionid.equals("")){
                    progress.dismissAllowingStateLoss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }else{
                    removeVirtualCardFromBankTask.execute(distributionid);
                }
            }

            @Override
            public void onFailed() {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
            }
        };

        final RegisterCardTask registerCardTask = new RegisterCardTask(getApplicationContext()) {
            @Override
            public void onSuccess() {
                String distributionid = PreferenceManager.getString(getApplicationContext(), Constant.PRERERENCE_CARD_DISTRIBUTION_ID, "");
                if(distributionid.equals("")){
                    progress.dismissAllowingStateLoss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }else{
                    marksAsVirtualCardTask.execute(distributionid);
                }
            }

            @Override
            public void onFailed() {
                //backToOrigin();
                progress.dismissAllowingStateLoss();
            }
        };

        GenerateVirtualCardTask generateVirtualCardTask = new GenerateVirtualCardTask(getApplicationContext()) {
            @Override
            public void onSuccess(AddVirtualResponseModel response) {
                PreferenceManager.putString(getApplicationContext(), Constant.PRERERENCE_CARD_DISTRIBUTION_ID, response.getDistribution_id());
                RegisterCardRequestModel body = new RegisterCardRequestModel();
                body.setCardNo(response.getDistribution_id());
                body.setCardName("Virtual Card");
                registerCardTask.execute(body);
            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
            }
        };
        generateVirtualCardTask.execute();
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

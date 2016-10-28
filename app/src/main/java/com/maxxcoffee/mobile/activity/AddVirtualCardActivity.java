package com.maxxcoffee.mobile.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.fragment.dialog.OptionDialog;
import com.maxxcoffee.mobile.model.request.RegisterCardRequestModel;
import com.maxxcoffee.mobile.model.response.AddVirtualResponseModel;
import com.maxxcoffee.mobile.task.AddVirtualCardTask;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rioswarawan on 7/19/16.
 */
public class AddVirtualCardActivity extends FragmentActivity {

    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.CAMERA};

    //@Bind(R.id.virtual_description)
    //WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_virtual_card);

        ButterKnife.bind(this);

        //setUsingWeb();
    }

    void setUsingWeb(){
        /*webView.loadUrl("file:///android_asset/demo/my_page.html");
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheMaxSize(1024*1024*8);
        webView.getSettings().setAppCachePath("/data/data/"+ getPackageName() +"/cache");
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setVisibility(View.GONE);*/
    }

    @OnClick(R.id.addVirtualCard)
    void onAddVirtualCardClick(){
        Bundle bundle = new Bundle();
        bundle.putString("content", "You only have 1 chance to get Virtual Card");
        bundle.putString("default", OptionDialog.CANCEL);

        OptionDialog optionDialog = new OptionDialog() {
            @Override
            protected void onOk() {
                dismiss();
                try {
                    if(Utils.isConnected(getApplicationContext())){
                        //getVirtualCard();
                        getTheVirtualCard();
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

    void getTheVirtualCard(){
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getSupportFragmentManager(), null);

        AddVirtualCardTask task = new AddVirtualCardTask(getApplicationContext()) {
            @Override
            public void onSuccess(AddVirtualResponseModel tos) {
                Toast.makeText(getApplicationContext(), "Virtual card successfully added", Toast.LENGTH_LONG).show();
                progress.dismissAllowingStateLoss();
                PreferenceManager.putBool(getApplicationContext(), Constant.PREFERENCE_ROUTE_CARD_SUCCESS, true);
                PreferenceManager.putInt(getApplicationContext(), Constant.PREFERENCE_CARD_AMOUNT, 1);
                //backToOrigin();
                backToMyCard();
            }

            @Override
            public void onFailed() {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                progress.dismissAllowingStateLoss();
            }

            @Override
            public void onFailed(AddVirtualResponseModel tos) {
                Toast.makeText(getApplicationContext(), tos.getMessages(), Toast.LENGTH_LONG).show();
                progress.dismissAllowingStateLoss();
            }
        };

        task.execute();
    }

    private void backToOrigin(){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                onBackClick();
            }
        }, 1500);
    }

    void backToMyCard(){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                PreferenceManager.putInt(getApplicationContext(), Constant.PREFERENCE_HAS_VIRTUAL_CARD, 1);
                PreferenceManager.putBool(getApplicationContext(), Constant.PREFERENCE_CARD_IS_LOADING, false);
                SimpleDateFormat df = new SimpleDateFormat(Constant.DATEFORMAT_META);
                Date today = new Date();
                String strToday = df.format(today);
                PreferenceManager.putString(getApplicationContext(), Constant.PREFERENCE_CARD_LAST_UPDATE, strToday);

                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                in.putExtra("content", MainActivity.MY_CARD);
                startActivity(in);
            }
        }, 1500);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.back)
    public void onBackClick() {
        onBackPressed();
    }

}

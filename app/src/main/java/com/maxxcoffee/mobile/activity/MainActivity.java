package com.maxxcoffee.mobile.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.adapter.DrawerAdapter;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.fragment.AboutFragment;
import com.maxxcoffee.mobile.fragment.CardHistoryFragment;
import com.maxxcoffee.mobile.fragment.CredentialFragment;
import com.maxxcoffee.mobile.fragment.EventFragment;
import com.maxxcoffee.mobile.fragment.FaqFragment;
import com.maxxcoffee.mobile.fragment.LoginFragment;
import com.maxxcoffee.mobile.fragment.LostCardFragment;
import com.maxxcoffee.mobile.fragment.MyCardFragment;
import com.maxxcoffee.mobile.fragment.HomeFragment;
import com.maxxcoffee.mobile.fragment.MenuFragment;
import com.maxxcoffee.mobile.fragment.ProfileFragment;
import com.maxxcoffee.mobile.fragment.PromoFragment;
import com.maxxcoffee.mobile.fragment.ContactUsFragment;
import com.maxxcoffee.mobile.fragment.RewardFragment;
import com.maxxcoffee.mobile.fragment.SignUpFragment;
import com.maxxcoffee.mobile.fragment.SignUpInfoFragment;
import com.maxxcoffee.mobile.fragment.StoreFragment;
import com.maxxcoffee.mobile.fragment.TosFragment;
import com.maxxcoffee.mobile.fragment.TransferBalanceFragment;
import com.maxxcoffee.mobile.fragment.dialog.GenderDialog;
import com.maxxcoffee.mobile.fragment.dialog.OptionDialog;
import com.maxxcoffee.mobile.model.ChildDrawerModel;
import com.maxxcoffee.mobile.model.ParentDrawerModel;
import com.maxxcoffee.mobile.model.request.GCMRequestModel;
import com.maxxcoffee.mobile.task.LogoutAllMyDevicesTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PermissionUtil;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FragmentActivity {

    private static String[] PERMISSIONS_LOCATION = {android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};
    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    public static final int HOME = 1000;
    public static final int MENU = 1001;
    public static final int STORE = 1002;
    public static final int PROMO = 1003;
    public static final int REWARD = 1004;
    public static final int BALANCE_TOPUP = 1005;
    public static final int TOPUP_HISTORY = 1006;
    public static final int MY_CARD = 1007;
    public static final int ADD_NEW_CARD = 1008;
    public static final int BALANCE_TRANSFER = 1009;
    public static final int CARD_HISTORY = 1010;
    public static final int REPORT_LOST_CARD = 1011;
    public static final int FAQ = 1012;
    public static final int TOS = 1013;
    public static final int CONTACT_US = 1014;
    public static final int TUTORIAL = 1015;
    public static final int PROFILE = 1016;
    public static final int LOGOUT = 1017;
    //    public static final int FAQ_DETAIL = 1018;
    public static final int DETAIL_CARD = 1019;
    public static final int CREDENTIAL = 1020;
    public static final int LOGIN = 1021;
    public static final int SIGNUP = 1022;
    public static final int SIGNUP_INFO = 1023;
    public static final int EVENT = 1024;
    public static final int ABOUT = 1025;

    public int activeFragmentFlag = -999;
    public boolean isDrawerExpanded = false;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.nav_view)
    ExpandableListView navigationList;
    @Bind(R.id.toolbar_layout)
    RelativeLayout toolbarLayout;
    @Bind(R.id.root_layout)
    LinearLayout rootLayout;
    @Bind(R.id.navbar_background)
    LinearLayout navbarBg;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.hamburger)
    ImageView hamburger;
    @Bind(R.id.refresh)
    ImageView refresh;

    private List<ParentDrawerModel> listDataHeader;
    private HashMap<ParentDrawerModel, List<ChildDrawerModel>> listDataChild;
    private DrawerAdapter adapter;
    private Integer selectedPage;
    private int lastExpandedPosition = -1;
    private GoogleApiClient googleApiClient;
    private boolean settingRequested;

    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        boolean showTutorial = PreferenceManager.getBool(this, Constant.PREFERENCE_TUTORIAL_SKIP, false);
        if (!showTutorial) {
            Intent intentTutorial = new Intent(this, TutorialActivity.class);
            startActivity(intentTutorial);
        }

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //klo tidak ada extra, langsung brarti home
        //ini akan terjadi ketika balik dari activity lain seperti formactivity dsb
        //klo tetap di mainactivity, kan cuma switchfragment dengan parameter yg sudah ditentukan user
        //siapp
        Integer content = getIntent().getIntExtra("content", HOME);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        adapter = new DrawerAdapter(this, listDataHeader, listDataChild);
        navigationList.setAdapter(adapter);
        navigationList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    navigationList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        navigationList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
                ParentDrawerModel model = listDataHeader.get(groupPosition);

                if (!model.isExpandable()) {
                    drawer.closeDrawer(GravityCompat.START);
                    if (selectedPage != model.getId())
                        switchFragment(model.getId());
                }
                return false;
            }
        });

        navigationList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                drawer.closeDrawer(GravityCompat.START);
                ChildDrawerModel model = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                if (selectedPage != model.getId())
                    switchFragment(model.getId());
                return false;
            }
        });

        refresh.setVisibility(View.GONE);

        prepareDrawerList();
        prepareBackground();

        boolean logoutnow = PreferenceManager.getBool(this, Constant.PREFERENCE_LOGOUT_NOW, false);
        if(logoutnow){
            //device ini aj yah
            //logoutThisDevice();
        }

        boolean routeFromTutorial = PreferenceManager.getBool(this, Constant.PREFERENCE_MAIN_FROM_TUTORIAL, false);
        if(routeFromTutorial){
            PreferenceManager.putBool(this, Constant.PREFERENCE_MAIN_FROM_TUTORIAL, false);
            switchFragment(LOGIN);
        }else{
            switchFragment(content);
        }

        AppUpdater appUpdater = new AppUpdater(this)
                .setDisplay(Display.DIALOG)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .setTitleOnUpdateAvailable("Update available")
                .setContentOnUpdateAvailable("Check out the latest version available on Google Play Store")
                .setButtonUpdate("Update Now")
                .setButtonDismiss(null)
                .setButtonDoNotShowAgain(null)
                ;
        appUpdater.start();

        /*new AlertDialog.Builder(MainActivity.this)
                .setTitle("Update available")
                .setMessage("Check out the latest version available on Play Store")
                .setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareDrawerList();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        int mDayPart = Utils.getDayPart();
        if (fragment instanceof HomeFragment) {
            if (mDayPart == Utils.MORNING) {
                setRootBackground(R.drawable.bg_morning);
                setNavbarBackground(R.drawable.bg_morning_navbar);
            } else if (mDayPart == Utils.AFTERNOON) {
                setRootBackground(R.drawable.bg_afternoon);
                setNavbarBackground(R.drawable.bg_afternoon_navbar);
            } else if (mDayPart == Utils.EVENING) {
                setRootBackground(R.drawable.bg_evening);
                setNavbarBackground(R.drawable.bg_evening_navbar);
            }
        }

        boolean logoutnow = PreferenceManager.getBool(this, Constant.PREFERENCE_LOGOUT_NOW, false);
        if(logoutnow){
            //device ini aj yah
            //logoutThisDevice();
        }
    }

    @OnClick(R.id.hamburger)
    public void onHamburgerClick() {
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        int mDayPart = Utils.getDayPart();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragment instanceof HomeFragment) {
            exitDialog();
        } else if (fragment instanceof MenuFragment
                || fragment instanceof StoreFragment
                || fragment instanceof PromoFragment
                || fragment instanceof EventFragment

                || fragment instanceof MyCardFragment
                || fragment instanceof TransferBalanceFragment
                || fragment instanceof CardHistoryFragment
                || fragment instanceof LostCardFragment

                || fragment instanceof AboutFragment
                || fragment instanceof FaqFragment
                || fragment instanceof TosFragment
                || fragment instanceof ContactUsFragment
                || fragment instanceof ProfileFragment

                || fragment instanceof RewardFragment
                || fragment instanceof CredentialFragment
                || fragment instanceof SignUpFragment
                || fragment instanceof LoginFragment) {
            switchFragment(HOME);
        }
//        else if (fragment instanceof SignUpFragment
//                || fragment instanceof LoginFragment) {
//            boolean fromLogin = PreferenceManager.getBool(this, Constant.PREFERENCE_ROUTE_FROM_LOGIN, false);
//            switchFragment(fromLogin ? LOGIN : CREDENTIAL);
//            if (fromLogin)
//                PreferenceManager.remove(this, Constant.PREFERENCE_ROUTE_FROM_LOGIN);
//        }
        else {
            super.onBackPressed();
        }
    }

    private void exitDialog() {
        Bundle bundle = new Bundle();
        bundle.putString("content", "Exit application?");
        bundle.putString("default", OptionDialog.CANCEL);

        OptionDialog optionDialog = new OptionDialog() {
            @Override
            protected void onOk() {
                moveTaskToBack(true);
                Process.killProcess(Process.myPid());
                System.exit(1);
            }

            @Override
            protected void onCancel() {
                dismiss();
            }
        };
        optionDialog.setArguments(bundle);
        optionDialog.show(getSupportFragmentManager(), null);
    }

    public void switchFragment(int contentId) {
        switchFragment(contentId, null);
    }

    public void switchFragment(int contentId, Bundle bundle) {
        try {
            selectedPage = contentId;

            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.content, getContent(contentId, bundle));
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Fragment getContent(int contentId, Bundle bundle) {
        //lihat fragment yg aktif
        setActiveFragmentFlag(contentId);

        boolean isLoggedIn = PreferenceManager.getBool(this, Constant.PREFERENCE_LOGGED_IN, false);
        boolean isSmsVerified = PreferenceManager.getBool(this, Constant.PREFERENCE_SMS_VERIFICATION, false);
        boolean isEmailVerified = PreferenceManager.getBool(this, Constant.PREFERENCE_EMAIL_VERIFICATION, false);

        Fragment fragment = null;
        switch (contentId) {
            case HOME:
                fragment = new HomeFragment();
                break;
            case STORE:
                if(!isGpsEnabled()){
                    settingRequested = false;
                }
                if (!settingRequested) {
                    checkSettingApi(STORE);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                            ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, 2);
                        } else {
                            ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, 2);
                        }
                    } else {
                        fragment = new StoreFragment();
                    }
                }
                break;
            case PROMO:
                fragment = new PromoFragment();
                break;
            case EVENT:
                fragment = new EventFragment();
                break;
            case MENU:
                fragment = new MenuFragment();
                break;
            case ABOUT:
                fragment = new AboutFragment();
                break;
            case MY_CARD:
                if (isLoggedIn) {
                    if (isSmsVerified && isEmailVerified) {
                        fragment = new MyCardFragment();
                    } else {
                        Intent intent = new Intent(this, VerificationActivity.class);
                        intent.putExtra("redirect-fragment", MY_CARD);

                        startActivity(intent);
                    }
                } else {
                    fragment = new CredentialFragment();
                }
                break;
            case FAQ:
                fragment = new FaqFragment();
                break;
            case TOS:
                fragment = new TosFragment();
                break;
            case CONTACT_US:
                if (isLoggedIn) {
                    if (isSmsVerified && isEmailVerified) {
                        fragment = new ContactUsFragment();
                    } else {
                        Intent intent = new Intent(this, VerificationActivity.class);
                        intent.putExtra("redirect-fragment", MY_CARD);

                        startActivity(intent);
                    }
                } else {
                    fragment = new CredentialFragment();
                }
                break;
            case REPORT_LOST_CARD:
                if (isLoggedIn) {
                    if (isSmsVerified && isEmailVerified) {
                        fragment = new LostCardFragment();
                    } else {
                        Intent intent = new Intent(this, VerificationActivity.class);
                        intent.putExtra("redirect-fragment", MY_CARD);

                        startActivity(intent);
                    }
                } else {
                    fragment = new CredentialFragment();
                }
                break;
            case CARD_HISTORY:
                if (isLoggedIn) {
                    if (isSmsVerified && isEmailVerified) {
                        fragment = new CardHistoryFragment();
                    } else {
                        Intent intent = new Intent(this, VerificationActivity.class);
                        intent.putExtra("redirect-fragment", MY_CARD);

                        startActivity(intent);
                    }
                } else {
                    fragment = new CredentialFragment();
                }
                break;
            case REWARD:
                if (isLoggedIn) {
                    if (isSmsVerified && isEmailVerified) {
                        fragment = new RewardFragment();
                    } else {
                        Intent intent = new Intent(this, VerificationActivity.class);
                        intent.putExtra("redirect-fragment", MY_CARD);

                        startActivity(intent);
                    }
                } else {
                    fragment = new CredentialFragment();
                }
                break;
            case CREDENTIAL:
                fragment = new CredentialFragment();
                break;
            case LOGIN:
                /*if(!isGpsEnabled()){
                    settingRequested = false;
                }*/
                /*if (!settingRequested) {
                    checkSettingApi(LOGIN);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                            ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, 3);
                        } else {
                            ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, 3);
                        }
                    } else {
                        //Toast.makeText(getApplicationContext(), "switc", Toast.LENGTH_LONG).show();
                        fragment = new LoginFragment();
                    }
                }*/
                fragment = new LoginFragment();
                break;
            case SIGNUP:
                /*if(!isGpsEnabled()){
                    settingRequested = false;
                }
                if (!settingRequested) {
                    checkSettingApi(SIGNUP);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                            ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, 4);
                        } else {
                            ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, 4);
                        }
                    } else {
                        fragment = new SignUpFragment();
                    }
                }*/
                fragment = new SignUpFragment();
                break;
            case SIGNUP_INFO:
                fragment = new SignUpInfoFragment();
                break;
            case PROFILE:
                fragment = isLoggedIn ? new ProfileFragment() : new CredentialFragment();
                break;
            case TUTORIAL:
                Intent intentTutorial = new Intent(this, TutorialActivity.class);
                startActivity(intentTutorial);
                break;
            case LOGOUT:
                logoutNow();
                break;
            case ADD_NEW_CARD:
                Toast.makeText(MainActivity.this, "Feature not available yet", Toast.LENGTH_SHORT).show();
                break;
            case BALANCE_TRANSFER:
                if (isLoggedIn) {
                    if (isSmsVerified && isEmailVerified) {
                        fragment = new TransferBalanceFragment();
                    } else {
                        Intent intent = new Intent(this, VerificationActivity.class);
                        intent.putExtra("redirect-fragment", MY_CARD);

                        startActivity(intent);
                    }
                } else {
                    fragment = new CredentialFragment();
                }
                break;
        }

        if (bundle != null)
            if (fragment != null)
                fragment.setArguments(bundle);

        return fragment;
    }

    private boolean checkSettingApi(final int requestCode) {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        settingRequested = true;
                        switchFragment(requestCode);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MainActivity.this, requestCode);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
        return false;
    }

    public void logoutNow() {
        logoutAllMyDevices();

        /*PreferenceManager.putBool(getApplicationContext(), Constant.PREFERENCE_LOGOUT_NOW, false);
        PreferenceManager.clearPreference(MainActivity.this);
        DatabaseConfig db = new DatabaseConfig(MainActivity.this);
        db.clearAllTable();
        prepareDrawerList();

        switchFragment(HOME);*/
    }

    public void logoutAllMyDevices(){
        if(Utils.isConnected(getApplicationContext())){
            GCMRequestModel body = new GCMRequestModel();
            body.setEmail(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_EMAIL, ""));

            LogoutAllMyDevicesTask task = new LogoutAllMyDevicesTask(getApplicationContext()) {
                @Override
                public void onSuccess(String message) {
                    logoutThisDevice();
                    Log.d("logoutallmydevices", message);
                }

                @Override
                public void onFailed() {
                    logoutThisDevice();
                    Log.d("logoutallmydevices", "failed");
                }
            };
            task.execute(body);
        }else {
            logoutThisDevice();
        }
    }

    void logoutThisDevice(){
        PreferenceManager.putBool(getApplicationContext(), Constant.PREFERENCE_LOGOUT_NOW, false);
        PreferenceManager.clearPreference(MainActivity.this);
        DatabaseConfig db = new DatabaseConfig(MainActivity.this);
        db.clearAllTable();
        prepareDrawerList();

        switchFragment(HOME);
    }

    public void setTitle(String mTitle) {
        title.setText(mTitle == null ? "" : mTitle);
        title.setTextColor(getResources().getColor(R.color.background_black));
    }

    //pake ubah color
    public void setTitle(String mTitle, boolean iswhite) {
        title.setText(mTitle == null ? "" : mTitle);
        title.setTextColor(getResources().getColor(R.color.background_white));
    }

    //overloading
    public void setHeaderColor(boolean transparent) {
        if (rootLayout != null) {
            refresh.setOnClickListener(null);
            refresh.setVisibility(View.GONE);
            if (transparent) {
                rootLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                hamburger.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_white));
            } else {
                rootLayout.setBackgroundColor(getResources().getColor(R.color.background_cream));
                hamburger.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_black));
            }
        }
    }

    //overloading
    public void setHeaderColor(boolean transparent, boolean showrefresh) {
        if (rootLayout != null) {
            if (transparent) {
                rootLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                hamburger.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_white));
            } else {
                rootLayout.setBackgroundColor(getResources().getColor(R.color.background_cream));
                hamburger.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_black));
            }
            if(showrefresh){
                refresh.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setRootBackground(int rootBackground) {
        if (rootLayout != null) {
            if (rootBackground != -999) {
                rootLayout.setBackgroundResource(rootBackground);
            }
        }
    }

    public void setNavbarBackground(int background) {
        if (navbarBg != null) {
            if (background != -999) {
                navbarBg.setBackgroundResource(background);
            }
        }
    }

    public void prepareDrawerList() {
        listDataHeader.clear();
        listDataChild.clear();
        boolean isLoggedIn = PreferenceManager.getBool(this, Constant.PREFERENCE_LOGGED_IN, false);
        int virtual = PreferenceManager.getInt(this, Constant.PREFERENCE_HAS_VIRTUAL_CARD, 0);

        //      PARENT
        ParentDrawerModel home = new ParentDrawerModel();
        home.setId(HOME);
        home.setName("Home");
        home.setExpandable(false);
        home.setIcon(R.drawable.ic_home);

        ParentDrawerModel browse = new ParentDrawerModel();
        browse.setId(2);
        browse.setName("Browse");
        browse.setExpandable(true);
        browse.setIcon(R.drawable.ic_coffee_white);

        ParentDrawerModel card = new ParentDrawerModel();
        card.setId(5);
        card.setName("Card");
        card.setExpandable(true);
        card.setIcon(R.drawable.ic_creditcard);

        ParentDrawerModel about = new ParentDrawerModel();
        about.setId(6);
        about.setName("About");
        about.setExpandable(true);
        about.setIcon(R.drawable.ic_maxx_small);

        ParentDrawerModel profile = new ParentDrawerModel();
        profile.setId(isLoggedIn ? PROFILE : SIGNUP);
        profile.setName(isLoggedIn ? "Profile" : "Sign Up");
        profile.setExpandable(false);
        profile.setIcon(R.drawable.ic_user);

        ParentDrawerModel logout = new ParentDrawerModel();
        logout.setId(isLoggedIn ? LOGOUT : LOGIN);
        logout.setName(isLoggedIn ? "Logout" : "Login");
        logout.setExpandable(false);
        logout.setIcon(R.drawable.ic_signout);

        listDataHeader.add(home);
        listDataHeader.add(browse);
        listDataHeader.add(card);
        listDataHeader.add(about);
        listDataHeader.add(profile);
        listDataHeader.add(logout);

        //      CHILD
        ChildDrawerModel childBrowse1 = new ChildDrawerModel();
        childBrowse1.setId(MENU);
        childBrowse1.setName("Menu");

        ChildDrawerModel childBrowse2 = new ChildDrawerModel();
        childBrowse2.setId(STORE);
        childBrowse2.setName("Store");

        ChildDrawerModel childBrowse3 = new ChildDrawerModel();
        childBrowse3.setId(PROMO);
        childBrowse3.setName("Promo");

        ChildDrawerModel childBrowse4 = new ChildDrawerModel();
        childBrowse4.setId(EVENT);
        childBrowse4.setName("Event");

        ChildDrawerModel childTransaction1 = new ChildDrawerModel();
        childTransaction1.setId(REWARD);
        childTransaction1.setName("Reward");

        ChildDrawerModel childCard1 = new ChildDrawerModel();
        childCard1.setId(MY_CARD);
        childCard1.setName("My Card");

        ChildDrawerModel childCard3 = new ChildDrawerModel();
        childCard3.setId(BALANCE_TRANSFER);
        childCard3.setName("Balance Transfer");

        ChildDrawerModel childCard4 = new ChildDrawerModel();
        childCard4.setId(CARD_HISTORY);
        childCard4.setName("Card History");

        ChildDrawerModel childCard5 = new ChildDrawerModel();
        childCard5.setId(REPORT_LOST_CARD);
        childCard5.setName("Report Lost Card");

        ChildDrawerModel childAbout1 = new ChildDrawerModel();
        childAbout1.setId(FAQ);
        childAbout1.setName("FAQ");

        ChildDrawerModel childAbout2 = new ChildDrawerModel();
        childAbout2.setId(TOS);
        childAbout2.setName("Terms of Service");

        ChildDrawerModel childAbout3 = new ChildDrawerModel();
        childAbout3.setId(CONTACT_US);
        childAbout3.setName("Contact Us");

        ChildDrawerModel childAbout4 = new ChildDrawerModel();
        childAbout4.setId(TUTORIAL);
        childAbout4.setName("Tutorial");

        ChildDrawerModel childAbout5 = new ChildDrawerModel();
        childAbout5.setId(ABOUT);
        childAbout5.setName("About");

        //      LIST
        List<ChildDrawerModel> listBrowse = new ArrayList<>();
        listBrowse.add(childBrowse1);
        listBrowse.add(childBrowse2);
        listBrowse.add(childBrowse3);
        listBrowse.add(childBrowse4);

        List<ChildDrawerModel> listCard = new ArrayList<>();
        listCard.add(childCard1);
        listCard.add(childCard4);
        //if(virtual == 0){
            listCard.add(childCard3);
            listCard.add(childCard5);
        //}

        List<ChildDrawerModel> listAbout = new ArrayList<>();
        listAbout.add(childAbout5);
        listAbout.add(childAbout1);
        listAbout.add(childAbout2);
        listAbout.add(childAbout3);
        if(!isLoggedIn){
            listAbout.add(childAbout4);
        }

        listDataChild.put(browse, listBrowse);
        listDataChild.put(card, listCard);
        listDataChild.put(about, listAbout);

        adapter.notifyDataSetChanged();
    }

    void prepareBackground(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        int mDayPart = Utils.getDayPart();
        if (fragment instanceof HomeFragment) {
            if (mDayPart == Utils.MORNING) {
                setRootBackground(R.drawable.bg_morning);
                setNavbarBackground(R.drawable.bg_morning_navbar);
            } else if (mDayPart == Utils.AFTERNOON) {
                setRootBackground(R.drawable.bg_afternoon);
                setNavbarBackground(R.drawable.bg_afternoon_navbar);
            } else if (mDayPart == Utils.EVENING) {
                setRootBackground(R.drawable.bg_evening);
                setNavbarBackground(R.drawable.bg_evening_navbar);
            }
        }else{
            if (mDayPart == Utils.MORNING) {
                //setRootBackground(R.drawable.bg_morning);
                setNavbarBackground(R.drawable.bg_morning_navbar);
            } else if (mDayPart == Utils.AFTERNOON) {
                //setRootBackground(R.drawable.bg_afternoon);
                setNavbarBackground(R.drawable.bg_afternoon_navbar);
            } else if (mDayPart == Utils.EVENING) {
                //setRootBackground(R.drawable.bg_evening);
                setNavbarBackground(R.drawable.bg_evening_navbar);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(getApplicationContext(), "Request code: " + String.valueOf(requestCode) + " "+ String.valueOf(resultCode), Toast.LENGTH_LONG).show();
        if(requestCode == STORE){
            switch (resultCode) {
                case Activity.RESULT_OK:
                    switchFragment(STORE);
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_enable_gps), Toast.LENGTH_LONG).show();
                    break;
            }
        }else if(requestCode == LOGIN){
            switch (resultCode) {
                case Activity.RESULT_OK:
                    switchFragment(LOGIN);
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_enable_gps), Toast.LENGTH_LONG).show();
                    break;
            }
        }else if(requestCode == SIGNUP){
            switch (resultCode) {
                case Activity.RESULT_OK:
                    switchFragment(SIGNUP);
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_enable_gps), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Toast.makeText(getApplicationContext(), "Request code permissionresult: " + String.valueOf(requestCode), Toast.LENGTH_LONG).show();
        if (requestCode == 2) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                switchFragment(STORE);
            } else {
                Toast.makeText(this, getResources().getString(R.string.please_enable_gps), Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == 3){
            if (PermissionUtil.verifyPermissions(grantResults)) {
                //Toast.makeText(getApplicationContext(), "granted", Toast.LENGTH_LONG).show();
                switchFragment(LOGIN);
            } else {
                Toast.makeText(this, getResources().getString(R.string.please_enable_gps), Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == 4){
            if (PermissionUtil.verifyPermissions(grantResults)) {
                switchFragment(SIGNUP);
            } else {
                Toast.makeText(this, getResources().getString(R.string.please_enable_gps), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public ImageView getRefresh() {
        return refresh;
    }

    public int getActiveFragmentFlag() {
        return activeFragmentFlag;
    }

    public void setActiveFragmentFlag(int activeFragmentFlag) {
        this.activeFragmentFlag = activeFragmentFlag;
    }

    public boolean isDrawerExpanded() {
        //cek drawer open atau tidak
       return drawer.isDrawerOpen(GravityCompat.START);
    }

    public void setDrawerExpanded(boolean drawerExpanded) {
        isDrawerExpanded = drawerExpanded;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isGpsEnabled(){
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);;
        boolean enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return enabled;
    }
}

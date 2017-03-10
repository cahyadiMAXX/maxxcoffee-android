package com.maxxcoffee.mobile.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.maxxcoffee.mobile.ui.about.AboutFragment;
import com.maxxcoffee.mobile.ui.cardhistory.CardHistoryFragment;
import com.maxxcoffee.mobile.ui.login.CredentialFragment;
import com.maxxcoffee.mobile.ui.event.EventFragment;
import com.maxxcoffee.mobile.ui.faq.FaqFragment;
import com.maxxcoffee.mobile.ui.login.LoginFragment;
import com.maxxcoffee.mobile.ui.report.LostCardFragment;
import com.maxxcoffee.mobile.ui.mycard.MyCardFragment;
import com.maxxcoffee.mobile.ui.home.HomeFragment;
import com.maxxcoffee.mobile.ui.menu.MenuFragment;
import com.maxxcoffee.mobile.ui.privacy.PrivacyFragment;
import com.maxxcoffee.mobile.ui.profile.ProfileFragment;
import com.maxxcoffee.mobile.ui.promo.PromoFragment;
import com.maxxcoffee.mobile.ui.contact.ContactUsFragment;
import com.maxxcoffee.mobile.ui.fragment.RewardFragment;
import com.maxxcoffee.mobile.ui.signup.SignUpFragment;
import com.maxxcoffee.mobile.ui.signup.SignUpInfoFragment;
import com.maxxcoffee.mobile.ui.store.StoreFragment;
import com.maxxcoffee.mobile.ui.tos.TosFragment;
import com.maxxcoffee.mobile.ui.balancetransfer.TransferBalanceFragment;
import com.maxxcoffee.mobile.ui.fragment.dialog.OptionDialog;
import com.maxxcoffee.mobile.model.ChildDrawerModel;
import com.maxxcoffee.mobile.model.ParentDrawerModel;
import com.maxxcoffee.mobile.model.request.GCMRequestModel;
import com.maxxcoffee.mobile.task.user.LogoutAllMyDevicesTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PermissionUtil;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;
import com.rampo.updatechecker.UpdateChecker;
import com.rampo.updatechecker.UpdateCheckerResult;
import com.rampo.updatechecker.store.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

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
    public static final int PRIVACY = 1026;

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

    //empty constructor
    public MainActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //if (Utils.isAllowed()){
            boolean showTutorial = PreferenceManager.getBool(this, Constant.PREFERENCE_TUTORIAL_SKIP, false);
            if (!showTutorial) {
                Intent intentTutorial = new Intent(this, TutorialActivity.class);
                startActivity(intentTutorial);
            }
        //}

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Integer content = getIntent().getIntExtra("content", HOME);

        /*Bundle bundle = new Bundle();
        String cc = getIntent().getStringExtra("contact");//PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_MAXX_CONCAT, "");
        bundle.putString("contact", cc);
        int cnt = PreferenceManager.getInt(getApplicationContext(), Constant.PREFERENCE_MAXX_CONTENT, 0);
        if (cnt != 0){
            content = cnt;
        }*/

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

        boolean routeFromTutorial = PreferenceManager.getBool(this, Constant.PREFERENCE_MAIN_FROM_TUTORIAL, false);
        if(routeFromTutorial){
            PreferenceManager.putBool(this, Constant.PREFERENCE_MAIN_FROM_TUTORIAL, false);
            switchFragment(LOGIN);
        }else{
            switchFragment(content);
        }
    }

    public void checkupdate(){
        UpdateChecker checker = new UpdateChecker(this, new UpdateCheckerResult() {
            @Override
            public void foundUpdateAndShowIt(String versionDonwloadable) {
                Timber.e("foundUpdateAndShowIt: %s", versionDonwloadable);
                showForceUpdate();
            }

            @Override
            public void foundUpdateAndDontShowIt(String versionDonwloadable) {
                Timber.e("foundUpdateAndDontShowIt: %s", versionDonwloadable);
                showForceUpdate();
            }

            @Override
            public void returnUpToDate(String versionDonwloadable) {
                Timber.e("returnUpToDate: %s", versionDonwloadable);
            }

            @Override
            public void returnMultipleApksPublished() {

            }

            @Override
            public void returnNetworkError() {

            }

            @Override
            public void returnAppUnpublished() {

            }

            @Override
            public void returnStoreError() {

            }
        });
        checker.setStore(Store.GOOGLE_PLAY);
        checker.start();
    }

    void showForceUpdate(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Update available")
                .setMessage("Check out the latest version available on Google Play Store")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String appPackageName = getApplicationContext().getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        moveTaskToBack(true);
                        Process.killProcess(Process.myPid());
                        System.exit(1);
                    }
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkupdate();
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
                || fragment instanceof LoginFragment
                || fragment instanceof PrivacyFragment) {
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
                if (checkFeaturedAvailable(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_STORE_STATUS, ""))) {
                    if (!isGpsEnabled()) {
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
                } else {
                    showFeaturedStatus(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_STORE_MESSAGE, ""));
                }
                break;
            case PROMO:
                if (checkFeaturedAvailable(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_PROMO_STATUS, ""))){
                    fragment = new PromoFragment();
                } else {
                    showFeaturedStatus(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_PROMO_MESSAGE, ""));
                }
                break;
            case EVENT:
                if (checkFeaturedAvailable(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_EVENT_STATUS, ""))){
                    fragment = new EventFragment();
                } else {
                    showFeaturedStatus(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_EVENT_MESSAGE, ""));
                }
                break;
            case MENU:
                if (checkFeaturedAvailable(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_MENU_STATUS, ""))){
                    fragment = new MenuFragment();
                } else {
                    showFeaturedStatus(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_MENU_MESSAGE, ""));
                }
                break;
            case ABOUT:
                fragment = new AboutFragment();
                break;
            case MY_CARD:
                if (checkFeaturedAvailable(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_MY_CARD_STATUS, ""))) {
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
                } else {
                    showFeaturedStatus(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_MY_CARD_MESSAGE, ""));
                }
                break;
            case FAQ:
                fragment = new FaqFragment();
                break;
            case TOS:
                fragment = new TosFragment();
                break;
            case PRIVACY:
                fragment = new PrivacyFragment();
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
                if (checkFeaturedAvailable(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_REPORT_STATUS, ""))) {
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
                } else {
                    showFeaturedStatus(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_REPORT_MESSAGE, ""));
                }
                break;
            case CARD_HISTORY:
                if (checkFeaturedAvailable(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_CARD_HISTORY_STATUS, ""))) {
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
                } else {
                    showFeaturedStatus(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_CARD_HISTORY_MESSAGE, ""));
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
                if (PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_LOGIN_STATUS, "").equals("yes")){
                    fragment = new LoginFragment();
                } else {
                    Toast.makeText(getApplicationContext(), PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_LOGIN_MESSAGE, ""), Toast.LENGTH_LONG).show();
                }
                break;
            case SIGNUP:
                if (PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_REGISTRATION_STATUS, "").equals("yes")){
                    fragment = new SignUpFragment();
                } else {
                    Toast.makeText(getApplicationContext(), PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_REGISTRATION_MESSAGE, ""), Toast.LENGTH_LONG).show();
                }
                break;
            case SIGNUP_INFO:
                fragment = new SignUpInfoFragment();
                break;
            case PROFILE:
                if (checkFeaturedAvailable(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_PROFILE_STATUS, ""))) {
                    fragment = isLoggedIn ? new ProfileFragment() : new CredentialFragment();
                } else {
                    showFeaturedStatus(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_PROMO_STATUS, ""));
                }
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
                if (checkFeaturedAvailable(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_BALANCE_TRANSFER_STATUS, ""))) {
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
                } else {
                    showFeaturedStatus(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_BALANCE_TRANSFER_MESSAGE, ""));
                }
                break;
        }

        if (bundle != null)
            if (fragment != null)
                fragment.setArguments(bundle);

        return fragment;
    }

    private boolean checkFeaturedAvailable(String status){
        return status.equals("yes");
    }

    private void showFeaturedStatus(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                }

                @Override
                public void onFailed() {
                    logoutThisDevice();
                }
            };
            task.execute(body);
        }else {
            logoutThisDevice();
        }
    }

    void logoutThisDevice(){
        boolean show_again = PreferenceManager.getBool(getApplicationContext(), Constant.PREFERENCE_SHOW_AGAIN, true);
        boolean is_rated = PreferenceManager.getBool(getApplicationContext(), Constant.PREFERENCE_HAS_RATED, false);
        PreferenceManager.putBool(getApplicationContext(), Constant.PREFERENCE_LOGOUT_NOW, false);
        PreferenceManager.clearPreference(MainActivity.this);
        DatabaseConfig db = new DatabaseConfig(MainActivity.this);
        db.clearAllTable();
        prepareDrawerList();

        //rating disimpan
        PreferenceManager.putBool(getApplicationContext(), Constant.PREFERENCE_SHOW_AGAIN, show_again);
        PreferenceManager.putBool(getApplicationContext(), Constant.PREFERENCE_HAS_RATED, is_rated);
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

        ChildDrawerModel childAbout6 = new ChildDrawerModel();
        childAbout6.setId(PRIVACY);
        childAbout6.setName("Privacy Policy");

        //      LIST
        List<ChildDrawerModel> listBrowse = new ArrayList<>();
        listBrowse.add(childBrowse1);
        listBrowse.add(childBrowse2);
        listBrowse.add(childBrowse3);
        listBrowse.add(childBrowse4);

        List<ChildDrawerModel> listCard = new ArrayList<>();
        listCard.add(childCard1);
        listCard.add(childCard4);
        listCard.add(childCard3);
        listCard.add(childCard5);

        List<ChildDrawerModel> listAbout = new ArrayList<>();
        listAbout.add(childAbout5);
        listAbout.add(childAbout1);
        listAbout.add(childAbout2);
        listAbout.add(childAbout6);
        listAbout.add(childAbout3);
        if(!isLoggedIn){
            //listAbout.add(childAbout4);
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
                    //switchFragment(HOME);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("content", HOME);
                    finish();
                    startActivity(intent);
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
                //switchFragment(STORE);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("content", STORE);
                finish();
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("content", HOME);
                finish();
                startActivity(intent);
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

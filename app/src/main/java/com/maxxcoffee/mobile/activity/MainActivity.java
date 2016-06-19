package com.maxxcoffee.mobile.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.adapter.DrawerAdapter;
import com.maxxcoffee.mobile.fragment.CredentialFragment;
import com.maxxcoffee.mobile.fragment.FaqDetailFragment;
import com.maxxcoffee.mobile.fragment.FaqFragment;
import com.maxxcoffee.mobile.fragment.LoginFragment;
import com.maxxcoffee.mobile.fragment.MyCardDetailFragment;
import com.maxxcoffee.mobile.fragment.MyCardFragment;
import com.maxxcoffee.mobile.fragment.HomeFragment;
import com.maxxcoffee.mobile.fragment.MenuFragment;
import com.maxxcoffee.mobile.fragment.ProfileFragment;
import com.maxxcoffee.mobile.fragment.PromoFragment;
import com.maxxcoffee.mobile.fragment.ReportFragment;
import com.maxxcoffee.mobile.fragment.RewardFragment;
import com.maxxcoffee.mobile.fragment.SignUpFragment;
import com.maxxcoffee.mobile.fragment.SignUpInfoFragment;
import com.maxxcoffee.mobile.fragment.StoreFragment;
import com.maxxcoffee.mobile.fragment.TosFragment;
import com.maxxcoffee.mobile.fragment.dialog.OptionDialog;
import com.maxxcoffee.mobile.model.ChildDrawerModel;
import com.maxxcoffee.mobile.model.ParentDrawerModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FragmentActivity {

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

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.nav_view)
    ExpandableListView navigationList;
    @Bind(R.id.toolbar_layout)
    RelativeLayout toolbarLayout;
    @Bind(R.id.root_layout)
    LinearLayout rootLayout;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.hamburger)
    ImageView hamburger;

    private List<ParentDrawerModel> listDataHeader;
    private HashMap<ParentDrawerModel, List<ChildDrawerModel>> listDataChild;
    private DrawerAdapter adapter;
    private Integer selectedPage;
    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        isLoggedIn = PreferenceManager.getBool(this, Constant.PREFERENCE_LOGGED_IN, false);
        Log.d("IS-LOGGED-IN", isLoggedIn + "");

        prepareDrawerList();
        adapter = new DrawerAdapter(this, listDataHeader, listDataChild);
        navigationList.setAdapter(adapter);
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
        switchFragment(HOME);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        int mDayPart = Utils.getDayPart();
        if (fragment instanceof HomeFragment) {
//            if (mDayPart == Utils.MORNING) {
//                rootLayout.setBackgroundResource(R.drawable.bg_landing);
//            } else if (mDayPart == Utils.AFTERNOON) {
//                rootLayout.setBackgroundResource(R.drawable.bg_landing);
//            } else if (mDayPart == Utils.EVENING) {
//                rootLayout.setBackgroundResource(R.drawable.bg_landing);
//            } else if (mDayPart == Utils.NIGHT) {
//                rootLayout.setBackgroundResource(R.drawable.bg_landing);
//            }
            rootLayout.setBackgroundResource(R.drawable.bg_landing);
        } else if (fragment instanceof LoginFragment
                || fragment instanceof SignUpInfoFragment
                || fragment instanceof SignUpFragment) {
            rootLayout.setBackgroundResource(R.drawable.bg_navbar);
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
        } else if (fragment instanceof StoreFragment
                || fragment instanceof MenuFragment
                || fragment instanceof PromoFragment
                || fragment instanceof MyCardFragment
                || fragment instanceof FaqFragment
                || fragment instanceof ProfileFragment
                || fragment instanceof TosFragment
                || fragment instanceof RewardFragment
                || fragment instanceof CredentialFragment
                || fragment instanceof ReportFragment) {
            switchFragment(HOME);
        } else if (fragment instanceof SignUpFragment
                || fragment instanceof LoginFragment) {
            boolean fromLogin = PreferenceManager.getBool(this, Constant.PREFERENCE_ROUTE_FROM_LOGIN, false);
            switchFragment(fromLogin ? LOGIN : CREDENTIAL);
            if (fromLogin)
                PreferenceManager.remove(this, Constant.PREFERENCE_ROUTE_FROM_LOGIN);
        } else {
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
                System.exit(0);
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
//            int mDayPart = Utils.getDayPart();
//            setBackgroundHome(contentId, mDayPart);

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
        Fragment fragment = null;
        switch (contentId) {
            case HOME:
                fragment = new HomeFragment();
                break;
            case STORE:
                fragment = new StoreFragment();
                break;
            case PROMO:
                fragment = new PromoFragment();
                break;
            case MENU:
                fragment = new MenuFragment();
                break;
            case MY_CARD:
                fragment = new MyCardFragment();
                break;
            case DETAIL_CARD:
                fragment = new MyCardDetailFragment();
                break;
            case FAQ:
                fragment = new FaqFragment();
                break;
            case TOS:
                fragment = new TosFragment();
                break;
            case CONTACT_US:
                bundle = new Bundle();
                bundle.putInt("selected-report", ReportFragment.COMPLAINT);

                fragment = new ReportFragment();
                break;
            case REPORT_LOST_CARD:
                bundle = new Bundle();
                bundle.putInt("selected-report", ReportFragment.LOST_CARD);

                fragment = new ReportFragment();
                break;
            case REWARD:
                fragment = new RewardFragment();
                break;
            case CREDENTIAL:
                fragment = new CredentialFragment();
                break;
            case LOGIN:
                fragment = new LoginFragment();
                break;
            case SIGNUP:
                fragment = new SignUpFragment();
                break;
            case SIGNUP_INFO:
                fragment = new SignUpInfoFragment();
                break;
            case PROFILE:
                fragment = new ProfileFragment();
                break;
        }

        if (bundle != null)
            if (fragment != null)
                fragment.setArguments(bundle);

        return fragment;
    }

    public void setTitle(String mTitle) {
        title.setText(mTitle == null ? "" : mTitle);
    }

    public void setHeaderColor(boolean transparent) {
        if (transparent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rootLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent, null));
                hamburger.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_white, null));
            } else {
                rootLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                hamburger.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_white));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rootLayout.setBackgroundColor(getResources().getColor(R.color.background_cream, null));
                hamburger.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_black, null));
            } else {
                rootLayout.setBackgroundColor(getResources().getColor(R.color.background_cream));
                hamburger.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_black));
            }
        }
    }

    public void setRootBackground(int rootBackground) {
        if (rootBackground != -999) {
            rootLayout.setBackgroundResource(rootBackground);
        }
    }

    private void prepareDrawerList() {
        //      PARENT
        ParentDrawerModel home = new ParentDrawerModel();
        home.setId(HOME);
        home.setName("Home");
        home.setExpandable(false);

        ParentDrawerModel browse = new ParentDrawerModel();
        browse.setId(2);
        browse.setName("Browse");
        browse.setExpandable(true);

        ParentDrawerModel transaction = new ParentDrawerModel();
        transaction.setId(3);
        transaction.setName("Transaction");
        transaction.setExpandable(true);

//        ParentDrawerModel topUp = new ParentDrawerModel();
//        topUp.setId(4);
//        topUp.setName("Top Up");
//        topUp.setExpandable(true);

        ParentDrawerModel card = new ParentDrawerModel();
        card.setId(5);
        card.setName("Card");
        card.setExpandable(true);

        ParentDrawerModel about = new ParentDrawerModel();
        about.setId(6);
        about.setName("About");
        about.setExpandable(true);

        ParentDrawerModel profile = new ParentDrawerModel();
        profile.setId(PROFILE);
        profile.setName("Profile");
        profile.setExpandable(false);

        ParentDrawerModel logout = new ParentDrawerModel();
        logout.setId(isLoggedIn ? LOGOUT : CREDENTIAL);
        logout.setName(isLoggedIn ? "Logout" : "Login");
        logout.setExpandable(false);

        listDataHeader.add(home);
        listDataHeader.add(browse);
        listDataHeader.add(transaction);
//        listDataHeader.add(topUp);
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

        ChildDrawerModel childTransaction1 = new ChildDrawerModel();
        childTransaction1.setId(REWARD);
        childTransaction1.setName("Reward");

//        ChildDrawerModel childTopUp1 = new ChildDrawerModel();
//        childTopUp1.setId(BALANCE_TOPUP);
//        childTopUp1.setName("Balance Top Up");
//
//        ChildDrawerModel childTopUp2 = new ChildDrawerModel();
//        childTopUp2.setId(TOPUP_HISTORY);
//        childTopUp2.setName("Top Up History");

        ChildDrawerModel childCard1 = new ChildDrawerModel();
        childCard1.setId(MY_CARD);
        childCard1.setName("My Card");

        ChildDrawerModel childCard2 = new ChildDrawerModel();
        childCard2.setId(ADD_NEW_CARD);
        childCard2.setName("Add New Card");

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
        childAbout2.setName("Term of Service");

        ChildDrawerModel childAbout3 = new ChildDrawerModel();
        childAbout3.setId(CONTACT_US);
        childAbout3.setName("Contact Us");

        ChildDrawerModel childAbout4 = new ChildDrawerModel();
        childAbout4.setId(TUTORIAL);
        childAbout4.setName("Tutorial");

        //      LIST
        List<ChildDrawerModel> listBrowse = new ArrayList<>();
        listBrowse.add(childBrowse1);
        listBrowse.add(childBrowse2);
        listBrowse.add(childBrowse3);

        List<ChildDrawerModel> listTransaction = new ArrayList<>();
        listTransaction.add(childTransaction1);

//        List<ChildDrawerModel> listTopUp = new ArrayList<>();
//        listTopUp.add(childTopUp1);
//        listTopUp.add(childTopUp2);

        List<ChildDrawerModel> listCard = new ArrayList<>();
        listCard.add(childCard1);
        listCard.add(childCard2);
        listCard.add(childCard3);
        listCard.add(childCard4);
        listCard.add(childCard5);

        List<ChildDrawerModel> listAbout = new ArrayList<>();
        listAbout.add(childAbout1);
        listAbout.add(childAbout2);
        listAbout.add(childAbout3);
        listAbout.add(childAbout4);

        listDataChild.put(browse, listBrowse);
        listDataChild.put(transaction, listTransaction);
//        listDataChild.put(topUp, listTopUp);
        listDataChild.put(card, listCard);
        listDataChild.put(about, listAbout);
    }
}

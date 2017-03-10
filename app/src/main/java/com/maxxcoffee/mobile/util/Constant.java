package com.maxxcoffee.mobile.util;

/**
 * Created by Rio Swarawan on 5/21/2016.
 */
public class Constant {
    public final static String DATEFORMAT_POST = "dd/MM/yyyy";
    public final static String DATEFORMAT_META = "yyyy-MM-dd HH:mm:ss";
    public final static String DATEFORMAT_STRING = "E, dd MMMM yyyy";
    public final static String DATEFORMAT_STRING_2 = "dd MMMM yyyy";
    public final static String DATEFORMAT_STRING_3 = "dd MMMM yyyy, HH:mm";
    public final static String DATEFORMAT_STRING_SIMPLE = "yyyy-MM-dd";


    public final static String PREFERENCE_MAXX = "maxx-coffee";
    public final static String PREFERENCE_TOKEN = "maxx-token";
    public final static String PREFERENCE_USER_NAME = "maxx-user-name";
    public final static String PREFERENCE_FIRST_NAME = "maxx-first-name";
    public final static String PREFERENCE_LAST_NAME = "maxx-last-name";
    public final static String PREFERENCE_PROFILE_CITY = "maxx-profile-city";
    public final static String PREFERENCE_PROFILE_REFERRAL = "maxx-profile-referral-code";
    public final static String PREFERENCE_PROFILE_OCCUPATION = "maxx-profile-occupation";
    public final static String PREFERENCE_ACCESS_TOKEN = "maxx-access_token";
    public final static String PREFERENCE_BALANCE = "maxx-balance";
    public final static String PREFERENCE_BEAN = "maxx-bean";
    public final static String PREFERENCE_GREETING = "maxx-greeting";
    public final static String PREFERENCE_EMAIL = "maxx-email";
    public final static String PREFERENCE_PHONE = "maxx-phone";
    public final static String PREFERENCE_WELCOME_SKIP = "maxx-welcome-skip";
    public final static String PREFERENCE_TUTORIAL_SKIP = "maxx-tutorial-skip";
    public final static String PREFERENCE_MAIN_FROM_TUTORIAL = "maxx-main-from-tutorial";
    public final static String PREFERENCE_LOGGED_IN = "maxx-logged-in";
    public final static String PREFERENCE_TOS = "maxx-tos";
    public final static String PREFERENCE_PRIVACY = "maxx-privacy";
    public final static String PREFERENCE_ABOUT = "maxx-about";
    public final static String PREFERENCE_SMS_VERIFICATION = "maxx-sms-verification";
    public final static String PREFERENCE_EMAIL_VERIFICATION = "maxx-email-verification";

    public final static String PREFERENCE_ROUTE_FROM_LOGIN = "maxx-route-from-login";
    public final static String PREFERENCE_ROUTE_TO_TRANSFER_BALANCE = "maxx-route-to-transfer-balance";
    public final static String PREFERENCE_ROUTE_TO_LOGOUT = "maxx-route-to-logout";

    public final static String PREFERENCE_REGISTER_FIRST_NAME = "maxx-register-first-name";
    public final static String PREFERENCE_REGISTER_IS_VALID_EMAIL = "maxx-register-is-valid-email";
    public final static String PREFERENCE_REGISTER_LAST_NAME = "maxx-register-last-name";
    public final static String PREFERENCE_REGISTER_EMAIL = "maxx-register-email";
    public final static String PREFERENCE_REGISTER_PHONE = "maxx-register-phone";
    public final static String PREFERENCE_REGISTER_PASSWORD = "maxx-register-password";
    public final static String PREFERENCE_REGISTER_GENDER = "maxx-register-gender";
    public final static String PREFERENCE_REGISTER_BIRTHDAY = "maxx-register-birthday";
    public final static String PREFERENCE_REGISTER_OCCUPATION = "maxx-register-occupation";
    public final static String PREFERENCE_REGISTER_CITY = "maxx-register-city";

    public final static String DATA_STORE = "maxx-data-store";
    public final static String DATA_KOTA = "maxx-kota";
    public final static String[] OCCUPATION_LIST =
            {
                    "Pelajar",
                    "Mahasiswa",
                    "Karyawan",
                    "Wiraswasta",
                    "Pegawai Negeri",
                    "Tenaga Medis",
                    "TNI / Polri",
                    "Mengurus rumah tangga",
                    "Lainnya"
            };

    //card sudah pernah load atau belum, jika belum maka load, jika sudah cek waktu
    //klo udah 30 menit yg lalu, silahkan loading, klo ga ambil yg lokal aj
    public final static String PREFERENCE_CARD_IS_LOADING = "maxx-card-is-loaded";
    public final static String PREFERENCE_CARD_LAST_UPDATE = "maxx-card-last-update";
    public final static String PREFERENCE_CARD_AMOUNT = "maxx-card-amount";
    public final static String PREFERENCE_ROUTE_CARD_SUCCESS = "maxx-route-from-card-success";

    //simpen latitude longitude
    public final static String PREFERENCE_LATITUDE_USER = "maxx-user-latitude";
    public final static String PREFERENCE_LONGITUDE_USER = "maxx-user-longitude";

    //verification code
    public final static String PREFERENCE_VERIFICATION_CODE = "maxx-verification-code";
    public final static String PRERERENCE_CARD_DISTRIBUTION_ID = "maxx-distribution-id";

    public final static String PREFERENCE_DEVICE_TOKEN = "maxx-device-token";
    public final static String PREFERENCE_LOGOUT_NOW = "maxx-logout-now";

    //virtual card
    public final static String PREFERENCE_HAS_VIRTUAL_CARD = "maxx-has-virtual-card";

    //force rating
    public final static String PREFERENCE_DATE_FIRST_LAUNCH = "maxx-first-launch";
    public final static String PREFERENCE_HAS_LAUNCH = "maxx-has-been-launch";
    public final static String PREFERENCE_SHOW_AGAIN = "maxx-dont-show-again";
    public final static String PREFERENCE_HAS_RATED = "maxx-has-rated";
    public final static int DAYS_UNTIL_PROMPT = 3;
    public final static int LAUNCHES_UNTIL_PROMPT = 7;


    //featured contol
    public final static String PREFERENCE_REGISTRATION_STATUS = "maxx-f-registration-status";
    public final static String PREFERENCE_REGISTRATION_MESSAGE = "maxx-f-registration-message";
    public final static String PREFERENCE_LOGIN_STATUS = "maxx-f-login-status";
    public final static String PREFERENCE_LOGIN_MESSAGE = "maxx-f-login-message";
    public final static String PREFERENCE_MY_CARD_STATUS = "maxx-f-my-card-status";
    public final static String PREFERENCE_MY_CARD_MESSAGE = "maxx-f-my-card-message";
    public final static String PREFERENCE_CARD_HISTORY_STATUS = "maxx-f-card-history-status";
    public final static String PREFERENCE_CARD_HISTORY_MESSAGE = "maxx-f-card-history-message";
    public final static String PREFERENCE_BALANCE_TRANSFER_STATUS = "maxx-f-balance-transfer-status";
    public final static String PREFERENCE_BALANCE_TRANSFER_MESSAGE = "maxx-f-balance-transfer-message";
    public final static String PREFERENCE_REPORT_STATUS = "maxx-f-report-status";
    public final static String PREFERENCE_REPORT_MESSAGE = "maxx-f-report-message";
    public final static String PREFERENCE_PROFILE_STATUS = "maxx-f-profile-status";
    public final static String PREFERENCE_PROFILE_MESSAGE = "maxx-f-profile-message";
    public final static String PREFERENCE_STORE_STATUS = "maxx-f-store-status";
    public final static String PREFERENCE_STORE_MESSAGE = "maxx-f-store-message";
    public final static String PREFERENCE_PROMO_STATUS = "maxx-f-promo-status";
    public final static String PREFERENCE_PROMO_MESSAGE = "maxx-f-promo-message";
    public final static String PREFERENCE_EVENT_STATUS = "maxx-f-event-status";
    public final static String PREFERENCE_EVENT_MESSAGE = "maxx-f-event-message";
    public final static String PREFERENCE_MENU_STATUS = "maxx-f-menu-status";
    public final static String PREFERENCE_MENU_MESSAGE = "maxx-f-menu-message";

    public final static String PREFERENCE_MAXX_CONTENT = "maxx-content";
    public final static String PREFERENCE_MAXX_CONCAT = "maxx-contact";

}

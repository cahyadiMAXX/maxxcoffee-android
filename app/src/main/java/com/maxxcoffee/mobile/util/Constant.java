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
}

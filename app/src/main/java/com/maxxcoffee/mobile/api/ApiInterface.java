package com.maxxcoffee.mobile.api;

import com.maxxcoffee.mobile.model.request.ChangeCityOccupationRequestModel;
import com.maxxcoffee.mobile.model.request.ChangeEmailRequestModel;
import com.maxxcoffee.mobile.model.request.ChangePhoneRequestModel;
import com.maxxcoffee.mobile.model.request.CheckValidEmailRequestModel;
import com.maxxcoffee.mobile.model.request.ContactUsRequestModel;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.request.DeleteCardRequestModel;
import com.maxxcoffee.mobile.model.request.GCMRequestModel;
import com.maxxcoffee.mobile.model.request.HistoryRequestModel;
import com.maxxcoffee.mobile.model.request.LoginRequestModel;
import com.maxxcoffee.mobile.model.request.LostCardRequestModel;
import com.maxxcoffee.mobile.model.request.MarkVirtualCardRequestModel;
import com.maxxcoffee.mobile.model.request.OauthRequestModel;
import com.maxxcoffee.mobile.model.request.PrimaryCardRequestModel;
import com.maxxcoffee.mobile.model.request.RegisterCardRequestModel;
import com.maxxcoffee.mobile.model.request.RegisterRequestModel;
import com.maxxcoffee.mobile.model.request.RenameCardRequestModel;
import com.maxxcoffee.mobile.model.request.ResendEmailRequestModel;
import com.maxxcoffee.mobile.model.request.StoreNearMeRequestModel;
import com.maxxcoffee.mobile.model.request.TransferBalanceRequestModel;
import com.maxxcoffee.mobile.model.request.VerifySmsCodeRequestModel;
import com.maxxcoffee.mobile.model.response.AboutResponseModel;
import com.maxxcoffee.mobile.model.response.AddVirtualResponseModel;
import com.maxxcoffee.mobile.model.response.CardResponseModel;
import com.maxxcoffee.mobile.model.response.ChangeUserDataResponseModel;
import com.maxxcoffee.mobile.model.response.CheckValidEmailResponseModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.model.response.EventResponseModel;
import com.maxxcoffee.mobile.model.response.FaqResponseModel;
import com.maxxcoffee.mobile.model.response.HistoryResponseModel;
import com.maxxcoffee.mobile.model.response.HomeResponseModel;
import com.maxxcoffee.mobile.model.response.KotaResponseModel;
import com.maxxcoffee.mobile.model.response.LoginResponseModel;
import com.maxxcoffee.mobile.model.response.LoginTestResponseModel;
import com.maxxcoffee.mobile.model.response.MenuResponseModel;
import com.maxxcoffee.mobile.model.response.OauthResponseModel;
import com.maxxcoffee.mobile.model.response.ProfileResponseModel;
import com.maxxcoffee.mobile.model.response.PromoResponseModel;
import com.maxxcoffee.mobile.model.response.RegisterResponseModel;
import com.maxxcoffee.mobile.model.response.ResendEmailSmsResponseModel;
import com.maxxcoffee.mobile.model.response.StoreResponseModel;
import com.maxxcoffee.mobile.model.response.TosResponseModel;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by rioswarawan on 7/6/16.
 */
public interface ApiInterface {

    @POST("/api/register")
    void register(@Body RegisterRequestModel body, Callback<RegisterResponseModel> response);

    @POST("/oauth/access_token")
    void oauth(@Body OauthRequestModel body, Callback<OauthResponseModel> response);

    @POST("/api/login")
    void login(@Header("Authorization") String authentication, @Body LoginRequestModel body, Callback<LoginResponseModel> response);

    @POST("/api/logintest")
    void loginTest(@Body LoginRequestModel body, Callback<LoginTestResponseModel> response);

    @GET("/api/menu/list")
    void menu(Callback<MenuResponseModel> response);

    @GET("/api/store/list")
    void store(Callback<StoreResponseModel> response);

    @POST("/api/store/cityuser")
    void storeCityUser(@Header("Authorization") String authentication, @Body DefaultRequestModel body, Callback<StoreResponseModel> response);

    @POST("/api/store/nearme")
    void storeNearMe(@Body StoreNearMeRequestModel body, Callback<StoreResponseModel> response);

    @GET("/api/promo/list")
    void promo(Callback<PromoResponseModel> response);

    @GET("/api/event/list")
    void event(Callback<EventResponseModel> response);

    //ngambil semua card
    @POST("/api/gci/cardlist")
    void cardList(@Header("Authorization") String authentication, @Body DefaultRequestModel body, Callback<CardResponseModel> response);

    //ngambil hanya 1 kartu
    @POST("/api/gci/carddetail")
    void cardListDetail(@Header("Authorization") String authentication, @Body PrimaryCardRequestModel body, Callback<CardResponseModel> response);

    //ngambil semua cardlocal
    @POST("/api/gci/card/list/local")
    void cardListLocal(@Header("Authorization") String authentication, @Body DefaultRequestModel body, Callback<CardResponseModel> response);

    @POST("/api/gci/registercard")
    void registerCard(@Header("Authorization") String authentication, @Body RegisterCardRequestModel body, Callback<CardResponseModel> response);

    @POST("/api/userprofile")
    void profile(@Header("Authorization") String authentication, @Body DefaultRequestModel body, Callback<ProfileResponseModel> response);

    @POST("/api/changepassword")
    void changePassword(@Header("Authorization") String authentication, @Body DefaultRequestModel body, Callback<ChangeUserDataResponseModel> response);

    @POST("/api/edituserprofilephone")
    void changeName(@Header("Authorization") String authentication, @Body DefaultRequestModel body, Callback<ChangeUserDataResponseModel> response);

    @POST("/api/changehpbyemail")
    void changePhone(@Header("Authorization") String authentication, @Body ChangePhoneRequestModel body, Callback<DefaultResponseModel> response);

    @POST("/api/gantinohpoccupation")
    void changeOldPhone(@Body ChangePhoneRequestModel body, Callback<DefaultResponseModel> response);

    @POST("/api/contactadd")
    void contactUs(@Header("Authorization") String authentication, @Body ContactUsRequestModel body, Callback<DefaultResponseModel> response);

    @POST("/api/gci/lostcard")
    void lostCard(@Header("Authorization") String authentication, @Body LostCardRequestModel body, Callback<DefaultResponseModel> response);

    @POST("/api/tos")
    void termOfService(Callback<TosResponseModel> response);

    @POST("/api/kota")
    void kota(Callback<KotaResponseModel> response);

    @POST("/api/faq")
    void faq(Callback<FaqResponseModel> response);

    @POST("/api/about")
    void about(Callback<AboutResponseModel> response);

    @POST("/api/gci/rename")
    void renameCard(@Header("Authorization") String authentication, @Body RenameCardRequestModel body, Callback<DefaultResponseModel> response);

    @POST("/api/gci/transferbalance")
    void transferBalance(@Header("Authorization") String authentication, @Body TransferBalanceRequestModel body, Callback<DefaultResponseModel> response);

    @POST("/api/resendemailsms")
    void resendEmailSms(@Header("Authorization") String authentication, @Body ResendEmailRequestModel body, Callback<ResendEmailSmsResponseModel> response);

    @POST("/api/verifysmsbyemail")
    void verifySmsCode(@Header("Authorization") String authentication, @Body VerifySmsCodeRequestModel body, Callback<DefaultResponseModel> response);

    //semua history card
    @POST("/api/gci/transaction/all")
    void history(@Header("Authorization") String authentication, @Body HistoryRequestModel body, Callback<HistoryResponseModel> response);

    //semua history card local
    @POST("/api/gci/card/list/local")
    void historyLocal(@Header("Authorization") String authentication, @Body HistoryRequestModel body, Callback<HistoryResponseModel> response);

    @POST("/api/deletecardbynumber")
    void deleteCard(@Header("Authorization") String authentication, @Body DeleteCardRequestModel body, Callback<DefaultResponseModel> response);

    @POST("/api/updatecityoccupation")
    void changeCityOccupation(@Header("Authorization") String authentication, @Body ChangeCityOccupationRequestModel body, Callback<DefaultResponseModel> response);

    @POST("/api/changeemail")
    void changeEmail(@Header("Authorization") String authentication, @Body ChangeEmailRequestModel body, Callback<DefaultResponseModel> response);

    @POST("/api/home")
    void home(@Header("Authorization") String authentication, @Body DefaultRequestModel body, Callback<HomeResponseModel> response);

    @POST("/api/forgotpassword")
    void forgotPassword(@Body ChangePhoneRequestModel body, Callback<DefaultResponseModel> response);

    @POST("/api/cek/email/exists")
    void checkEmailExist(@Body CheckValidEmailRequestModel body, Callback<CheckValidEmailResponseModel> response);

    //set prime card
    @POST("/api/setprimarycard")
    void setPrimaryCard(@Header("Authorization") String authentication, @Body PrimaryCardRequestModel body, Callback<DefaultResponseModel> response);

    //virtual card

    @POST("/api/gci/card/virtual/generate")
    void generateVirtualCard(@Header("Authorization") String authentication, @Body DefaultRequestModel body, Callback<AddVirtualResponseModel> response);

    @POST("/api/gci/card/virtual/mark")
    void markAsVirtualCard(@Header("Authorization") String authentication, @Body MarkVirtualCardRequestModel body, Callback<DefaultResponseModel> response);

    @POST("/api/gci/card/virtual/remove")
    void removeVirtualFromBank(@Header("Authorization") String authentication, @Body MarkVirtualCardRequestModel body, Callback<DefaultResponseModel> response);

    @POST("/api/updatedevicetoken")
    void updateDeviceToken(@Header("Authorization") String authentication, @Body GCMRequestModel body, Callback<DefaultResponseModel> response);

    @POST("/api/forcelogoutall")
    void forceLogoutAll(@Header("Authorization") String authentication, @Body GCMRequestModel body, Callback<DefaultResponseModel> response);
}

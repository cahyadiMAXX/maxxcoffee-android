package com.maxxcoffee.mobile.api;

import com.maxxcoffee.mobile.model.request.ContactUsRequestModel;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.request.LoginRequestModel;
import com.maxxcoffee.mobile.model.request.OauthRequestModel;
import com.maxxcoffee.mobile.model.request.RegisterRequestModel;
import com.maxxcoffee.mobile.model.request.StoreNearMeRequestModel;
import com.maxxcoffee.mobile.model.response.CardResponseModel;
import com.maxxcoffee.mobile.model.response.ChangeUserDataResponseModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.model.response.EventResponseModel;
import com.maxxcoffee.mobile.model.response.LoginResponseModel;
import com.maxxcoffee.mobile.model.response.MenuResponseModel;
import com.maxxcoffee.mobile.model.response.OauthResponseModel;
import com.maxxcoffee.mobile.model.response.ProfileResponseModel;
import com.maxxcoffee.mobile.model.response.PromoResponseModel;
import com.maxxcoffee.mobile.model.response.RegisterResponseModel;
import com.maxxcoffee.mobile.model.response.StoreResponseModel;

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

    @POST("/api/gci/cardlist")
    void cardList(@Header("Authorization") String authentication, @Body DefaultRequestModel body, Callback<CardResponseModel> response);

    @POST("/api/userprofile")
    void profile(@Header("Authorization") String authentication, @Body DefaultRequestModel body, Callback<ProfileResponseModel> response);

    @POST("/api/changepassword")
    void changePassword(@Header("Authorization") String authentication, @Body DefaultRequestModel body, Callback<ChangeUserDataResponseModel> response);

    @POST("/api/edituserprofilephone")
    void changeName(@Header("Authorization") String authentication, @Body DefaultRequestModel body, Callback<ChangeUserDataResponseModel> response);

    @POST("/api/contactadd")
    void contactUs(@Header("Authorization") String authentication, @Body ContactUsRequestModel body, Callback<DefaultResponseModel> response);

}

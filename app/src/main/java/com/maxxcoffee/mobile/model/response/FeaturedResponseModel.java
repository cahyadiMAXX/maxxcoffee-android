package com.maxxcoffee.mobile.model.response;

import com.maxxcoffee.mobile.model.FeaturedItemModel;

/**
 * Created by jemsnaban on 12/15/16.
 */

public class FeaturedResponseModel {

    private FeaturedItemModel registration;
    private FeaturedItemModel login;
    private FeaturedItemModel browse_menu;
    private FeaturedItemModel browse_store;
    private FeaturedItemModel browse_promo;
    private FeaturedItemModel browse_event;
    private FeaturedItemModel card;
    private FeaturedItemModel my_card;
    private FeaturedItemModel card_history;
    private FeaturedItemModel balance_transfer;
    private FeaturedItemModel report_lost_card;
    private FeaturedItemModel profile;

    public FeaturedItemModel getRegistration() {
        return registration;
    }

    public void setRegistration(FeaturedItemModel registration) {
        this.registration = registration;
    }

    public FeaturedItemModel getLogin() {
        return login;
    }

    public void setLogin(FeaturedItemModel login) {
        this.login = login;
    }

    public FeaturedItemModel getBrowse_menu() {
        return browse_menu;
    }

    public void setBrowse_menu(FeaturedItemModel browse_menu) {
        this.browse_menu = browse_menu;
    }

    public FeaturedItemModel getBrowse_store() {
        return browse_store;
    }

    public void setBrowse_store(FeaturedItemModel browse_store) {
        this.browse_store = browse_store;
    }

    public FeaturedItemModel getBrowse_promo() {
        return browse_promo;
    }

    public void setBrowse_promo(FeaturedItemModel browse_promo) {
        this.browse_promo = browse_promo;
    }

    public FeaturedItemModel getBrowse_event() {
        return browse_event;
    }

    public void setBrowse_event(FeaturedItemModel browse_event) {
        this.browse_event = browse_event;
    }

    public FeaturedItemModel getCard() {
        return card;
    }

    public void setCard(FeaturedItemModel card) {
        this.card = card;
    }

    public FeaturedItemModel getMy_card() {
        return my_card;
    }

    public void setMy_card(FeaturedItemModel my_card) {
        this.my_card = my_card;
    }

    public FeaturedItemModel getCard_history() {
        return card_history;
    }

    public void setCard_history(FeaturedItemModel card_history) {
        this.card_history = card_history;
    }

    public FeaturedItemModel getBalance_transfer() {
        return balance_transfer;
    }

    public void setBalance_transfer(FeaturedItemModel balance_transfer) {
        this.balance_transfer = balance_transfer;
    }

    public FeaturedItemModel getReport_lost_card() {
        return report_lost_card;
    }

    public void setReport_lost_card(FeaturedItemModel report_lost_card) {
        this.report_lost_card = report_lost_card;
    }

    public FeaturedItemModel getProfile() {
        return profile;
    }

    public void setProfile(FeaturedItemModel profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "FeaturedResponseModel{" +
                "registration=" + registration +
                ", login=" + login +
                ", browse_menu=" + browse_menu +
                ", browse_store=" + browse_store +
                ", browse_promo=" + browse_promo +
                ", browse_event=" + browse_event +
                ", card=" + card +
                ", my_card=" + my_card +
                ", card_history=" + card_history +
                ", balance_transfer=" + balance_transfer +
                ", report_lost_card=" + report_lost_card +
                ", profile=" + profile +
                '}';
    }
}

package com.maxxcoffee.mobile.model.response;

import java.util.List;

/**
 * Created by rioswarawan on 7/31/16.
 */
public class KotaResponseModel {

    private List<KotaItemResponseModel> result;
    private List<KotaItemResponseModel> listprovinsi;

    public List<KotaItemResponseModel> getResult() {
        return result;
    }

    public void setResult(List<KotaItemResponseModel> result) {
        this.result = result;
    }

    public List<KotaItemResponseModel> getListprovinsi() {
        return listprovinsi;
    }

    public void setListprovinsi(List<KotaItemResponseModel> listprovinsi) {
        this.listprovinsi = listprovinsi;
    }
}

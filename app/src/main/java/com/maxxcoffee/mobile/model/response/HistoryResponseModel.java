package com.maxxcoffee.mobile.model.response;

import java.util.List;

/**
 * Created by rioswarawan on 7/27/16.
 */
public class HistoryResponseModel {
    private String status;
    private List<HistoryItemResponseModel> transaksi;
    private List<HistoryItemResponseModel> transferBalance;
    private List<HistoryItemResponseModel> penukaran_point;
    private List<HistoryItemResponseModel> topup;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<HistoryItemResponseModel> getTransaksi() {
        return transaksi;
    }

    public void setTransaksi(List<HistoryItemResponseModel> transaksi) {
        this.transaksi = transaksi;
    }

    public List<HistoryItemResponseModel> getTransferBalance() {
        return transferBalance;
    }

    public void setTransferBalance(List<HistoryItemResponseModel> transferBalance) {
        this.transferBalance = transferBalance;
    }

    public List<HistoryItemResponseModel> getPenukaran_point() {
        return penukaran_point;
    }

    public void setPenukaran_point(List<HistoryItemResponseModel> penukaran_point) {
        this.penukaran_point = penukaran_point;
    }

    public List<HistoryItemResponseModel> getTopup() {
        return topup;
    }

    public void setTopup(List<HistoryItemResponseModel> topup) {
        this.topup = topup;
    }
}

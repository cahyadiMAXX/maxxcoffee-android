package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 7/27/16.
 */
public class HistoryRequestModel extends DefaultRequestModel{

    private String card_number;
    private String periode_start;
    private String periode_end;

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getPeriode_start() {
        return periode_start;
    }

    public void setPeriode_start(String periode_start) {
        this.periode_start = periode_start;
    }

    public String getPeriode_end() {
        return periode_end;
    }

    public void setPeriode_end(String periode_end) {
        this.periode_end = periode_end;
    }

    @Override
    public String toString() {
        return "HistoryRequestModel{" +
                "card_number='" + card_number + '\'' +
                ", periode_start='" + periode_start + '\'' +
                ", periode_end='" + periode_end + '\'' +
                '}';
    }
}

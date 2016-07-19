package com.maxxcoffee.mobile.model.response;

import java.util.List;

/**
 * Created by rioswarawan on 7/16/16.
 */
public class VarianResponseModel {
    List<VarianItem> None;
    List<VarianItem> Hot;
    List<VarianItem> Iced;

    public List<VarianItem> getNone() {
        return None;
    }

    public void setNone(List<VarianItem> none) {
        None = none;
    }

    public List<VarianItem> getHot() {
        return Hot;
    }

    public void setHot(List<VarianItem> hot) {
        Hot = hot;
    }

    public List<VarianItem> getIced() {
        return Iced;
    }

    public void setIced(List<VarianItem> iced) {
        Iced = iced;
    }

    public class VarianItem {
        private String size;
        private String price;

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}

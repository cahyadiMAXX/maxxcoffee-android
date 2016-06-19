package com.maxxcoffee.mobile.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Rio Swarawan on 5/10/2016.
 */

@DatabaseTable(tableName = "store_item")
public class StoreItemEntity {

    @DatabaseField(id = true)
    Integer id;

    @DatabaseField
    Integer storeId;

    @DatabaseField
    String name;

    @DatabaseField
    String address;

    @DatabaseField
    String contact;

    @DatabaseField
    String open;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }
}

package com.maxxcoffee.mobile.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Rio Swarawan on 5/25/2016.
 */

@DatabaseTable(tableName = "menu")
public class MenuEntity {

    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String available_size;

    @DatabaseField
    private String available_type;

    @DatabaseField
    private String description;

    @DatabaseField
    private String image;

    @DatabaseField
    private Integer redeem_point;

    @DatabaseField
    private String status;

    @DatabaseField
    private String group;

    @DatabaseField
    private Integer category_id;

    @DatabaseField
    private String category;

    @DatabaseField
    private String tags;

    @DatabaseField
    private String price_hot;

    @DatabaseField
    private String price_iced;

    @DatabaseField
    private String price_none;

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

    public String getAvailable_size() {
        return available_size;
    }

    public void setAvailable_size(String available_size) {
        this.available_size = available_size;
    }

    public String getAvailable_type() {
        return available_type;
    }

    public void setAvailable_type(String available_type) {
        this.available_type = available_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getRedeem_point() {
        return redeem_point;
    }

    public void setRedeem_point(Integer redeem_point) {
        this.redeem_point = redeem_point;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPrice_hot() {
        return price_hot;
    }

    public void setPrice_hot(String price_hot) {
        this.price_hot = price_hot;
    }

    public String getPrice_iced() {
        return price_iced;
    }

    public void setPrice_iced(String price_iced) {
        this.price_iced = price_iced;
    }

    public String getPrice_none() {
        return price_none;
    }

    public void setPrice_none(String price_none) {
        this.price_none = price_none;
    }
}

package com.maxxcoffee.mobile.model;

/**
 * Created by Rio Swarawan on 5/20/2016.
 */
public class ParentDrawerModel {

    private Integer id;
    private String name;
    private Integer icon;
    private boolean expandable;

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

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }
}

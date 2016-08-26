package com.maxxcoffee.mobile.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Rio Swarawan on 5/29/2016.
 */

@DatabaseTable(tableName = "province")
public class ProvinceEntity {

    @DatabaseField(id = true)
    private String name;
//
//    @DatabaseField
//    private String province;
//
//    @DatabaseField
//    private String island;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getProvince() {
//        return province;
//    }
//
//    public void setProvince(String province) {
//        this.province = province;
//    }
//
//    public String getIsland() {
//        return island;
//    }
//
//    public void setIsland(String island) {
//        this.island = island;
//    }
}

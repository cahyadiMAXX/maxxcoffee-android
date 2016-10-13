package com.maxxcoffee.mobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.database.entity.CardPrimaryEntity;
import com.maxxcoffee.mobile.database.entity.CityEntity;
import com.maxxcoffee.mobile.database.entity.ProvinceEntity;
import com.maxxcoffee.mobile.database.entity.EventEntity;
import com.maxxcoffee.mobile.database.entity.FaqEntity;
import com.maxxcoffee.mobile.database.entity.HistoryEntity;
import com.maxxcoffee.mobile.database.entity.MenuCategoryEntity;
import com.maxxcoffee.mobile.database.entity.MenuEntity;
import com.maxxcoffee.mobile.database.entity.ProfileEntity;
import com.maxxcoffee.mobile.database.entity.PromoEntity;
import com.maxxcoffee.mobile.database.entity.StoreEntity;

import java.sql.SQLException;

/**
 * Created by Rio Swarawan on 5/10/2016.
 */
public class DatabaseConfig extends OrmLiteSqliteOpenHelper {

    private static final int DATABASE_VERSION = 41;
    private static final String DATABASE_NAME = "db_maxx";
    private static final String TAG = "MAXX-DATABASE";

    private Dao<StoreEntity, Integer> storeDao = null;
    private Dao<PromoEntity, Integer> promoDao = null;
    private Dao<MenuCategoryEntity, Integer> menuCategoryDao = null;
    private Dao<MenuEntity, Integer> menuItemDao = null;
    private Dao<CardEntity, Integer> cardDao = null;
    private Dao<CardPrimaryEntity, Integer> cardPrimaryDao = null;
    private Dao<FaqEntity, Integer> faqDao = null;
    private Dao<ProfileEntity, Integer> profileDao = null;
    private Dao<EventEntity, Integer> eventDao = null;
    private Dao<HistoryEntity, Integer> historyDao = null;
    private Dao<ProvinceEntity, Integer> provinceDao = null;
    private Dao<CityEntity, Integer> cityDao = null;

    public DatabaseConfig(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        init();
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        dropUpdatedTable();
        init();
    }

    private void init() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, StoreEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, PromoEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, MenuCategoryEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, MenuEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, CardEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, CardPrimaryEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, FaqEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, ProfileEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, EventEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, HistoryEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, ProvinceEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, CityEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dropUpdatedTable() {
        try {
            TableUtils.dropTable(connectionSource, StoreEntity.class, false);
            TableUtils.dropTable(connectionSource, PromoEntity.class, false);
            TableUtils.dropTable(connectionSource, MenuCategoryEntity.class, false);
            TableUtils.dropTable(connectionSource, MenuEntity.class, false);
            TableUtils.dropTable(connectionSource, CardEntity.class, false);
            TableUtils.dropTable(connectionSource, CardPrimaryEntity.class, false);
            TableUtils.dropTable(connectionSource, FaqEntity.class, false);
            TableUtils.dropTable(connectionSource, ProfileEntity.class, false);
            TableUtils.dropTable(connectionSource, EventEntity.class, false);
            TableUtils.dropTable(connectionSource, HistoryEntity.class, false);
            TableUtils.dropTable(connectionSource, ProvinceEntity.class, false);
            TableUtils.dropTable(connectionSource, CityEntity.class, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearAllTable() {
        try {
            TableUtils.clearTable(connectionSource, StoreEntity.class);
            TableUtils.clearTable(connectionSource, PromoEntity.class);
            TableUtils.clearTable(connectionSource, MenuCategoryEntity.class);
            TableUtils.clearTable(connectionSource, MenuEntity.class);
            TableUtils.clearTable(connectionSource, CardEntity.class);
            TableUtils.clearTable(connectionSource, CardPrimaryEntity.class);
            TableUtils.clearTable(connectionSource, FaqEntity.class);
            TableUtils.clearTable(connectionSource, ProfileEntity.class);
            TableUtils.clearTable(connectionSource, EventEntity.class);
            TableUtils.clearTable(connectionSource, HistoryEntity.class);
            TableUtils.clearTable(connectionSource, ProvinceEntity.class);
            TableUtils.clearTable(connectionSource, CityEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T> void clearTable(Class<T> dataClass) {
        try {
            TableUtils.clearTable(connectionSource, dataClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<StoreEntity, Integer> getStoreDao() throws SQLException {
        if (storeDao == null) {
            storeDao = getDao(StoreEntity.class);
        }
        return storeDao;
    }

    public Dao<PromoEntity, Integer> getPromoDao() throws SQLException {
        if (promoDao == null) {
            promoDao = getDao(PromoEntity.class);
        }
        return promoDao;
    }

    public Dao<MenuCategoryEntity, Integer> getMenuCategoryDao() throws SQLException {
        if (menuCategoryDao == null) {
            menuCategoryDao = getDao(MenuCategoryEntity.class);
        }
        return menuCategoryDao;
    }

    public Dao<MenuEntity, Integer> getMenuItemDao() throws SQLException {
        if (menuItemDao == null) {
            menuItemDao = getDao(MenuEntity.class);
        }
        return menuItemDao;
    }

    public Dao<CardEntity, Integer> getCardDao() throws SQLException {
        if (cardDao == null) {
            cardDao = getDao(CardEntity.class);
        }
        return cardDao;
    }

    public Dao<CardPrimaryEntity, Integer> getCardPrimaryDao() throws SQLException {
        if (cardPrimaryDao == null) {
            cardPrimaryDao = getDao(CardPrimaryEntity.class);
        }
        return cardPrimaryDao;
    }

    public Dao<FaqEntity, Integer> getFaqDao() throws SQLException {
        if (faqDao == null) {
            faqDao = getDao(FaqEntity.class);
        }
        return faqDao;
    }

    public Dao<ProfileEntity, Integer> getProfileDao() throws SQLException {
        if (profileDao == null) {
            profileDao = getDao(ProfileEntity.class);
        }
        return profileDao;
    }

    public Dao<EventEntity, Integer> getEventDao() throws SQLException {
        if (eventDao == null) {
            eventDao = getDao(EventEntity.class);
        }
        return eventDao;
    }

    public Dao<HistoryEntity, Integer> getHistoryDao() throws SQLException {
        if (historyDao == null) {
            historyDao = getDao(HistoryEntity.class);
        }
        return historyDao;
    }

    public Dao<ProvinceEntity, Integer> getProvinceDao() throws SQLException {
        if (provinceDao == null) {
            provinceDao = getDao(ProvinceEntity.class);
        }
        return provinceDao;
    }

    public Dao<CityEntity, Integer> getCityDao() throws SQLException {
        if (cityDao == null) {
            cityDao = getDao(CityEntity.class);
        }
        return cityDao;
    }
}

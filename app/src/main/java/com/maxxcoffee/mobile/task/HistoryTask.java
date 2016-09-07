package com.maxxcoffee.mobile.task;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.database.controller.HistoryController;
import com.maxxcoffee.mobile.database.entity.HistoryEntity;
import com.maxxcoffee.mobile.model.request.HistoryRequestModel;
import com.maxxcoffee.mobile.model.response.AboutResponseModel;
import com.maxxcoffee.mobile.model.response.HistoryItemResponseModel;
import com.maxxcoffee.mobile.model.response.HistoryResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class HistoryTask extends AsyncTask<HistoryRequestModel, Boolean, HistoryResponseModel> {

    private Context context;
    private HistoryController historyController;

    public HistoryTask(Context context) {
        this.context = context;
        this.historyController = new HistoryController(context);
    }

    @Override
    protected HistoryResponseModel doInBackground(HistoryRequestModel... data) {

        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");


        HistoryRequestModel body = data[0];
        body.setToken(token);

        //toe di e
        ApiManager.getApiInterface(context).history(accessToken, body, new Callback<HistoryResponseModel>() {
            @Override
            public void success(HistoryResponseModel storeResponseModel, Response response) {
                onPostExecute(storeResponseModel);
            }

            @Override
            public void failure(RetrofitError error) {
                onFailed();
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(HistoryResponseModel response) {
        super.onPostExecute(response);
        if (response != null) {
            if (response.getStatus().equals("success")) {

                int transferBalanceSize = response.getTransferBalance().size();
                int transactionSize = response.getTransaksi().size();
                int pointSize = response.getPenukaran_point().size();
                int topupSize = response.getTopup().size();

                historyController.clear();

                if (transactionSize > 0) {
                    saveData(response.getTransaksi());
                }
                if (transferBalanceSize > 0) {
                    saveData(response.getTransferBalance());
                }
                if (pointSize > 0) {
                    saveData(response.getPenukaran_point());
                }
                if (topupSize > 0) {
                    saveData(response.getTopup());
                }
                onSuccess();
            } else {
                onFailed();
            }
        }
    }

    private void saveData(List<HistoryItemResponseModel> data) {
        for (HistoryItemResponseModel item : data) {
            HistoryEntity entity = new HistoryEntity();
            entity.setTransaction_no(item.getTransaction_no());
            entity.setCard_no(item.getCard_no());
            entity.setType(item.getType());
            entity.setStore(item.getStore());
            entity.setAmount(item.getAmount());
            entity.setStatus(item.getStatus());
            entity.setDescription(item.getDescription());
            entity.setTime(item.getTime());

            historyController.insert(entity);
        }
    }

    public abstract void onSuccess();

    public abstract void onFailed();
}

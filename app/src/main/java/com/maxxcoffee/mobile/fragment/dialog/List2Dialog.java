package com.maxxcoffee.mobile.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.adapter.DialogListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by Rio Swarawan on 3/13/2016.
 */
public abstract class List2Dialog extends DialogFragment {

    @Bind(R.id.list)
    ListView list;
    @Bind(R.id.title)
    TextView title;

    private List<String> data;
    private DialogListAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_list);
        dialog.show();

        ButterKnife.bind(this, dialog);

        String mTitle = getArguments().getString("title", "");
        title.setText(mTitle);

        data = new ArrayList<>();
        adapter = new DialogListAdapter(getActivity(), data);
        list.setAdapter(adapter);

        getItems();

        return dialog;
    }

    private void getItems() {
        String jsonData = getArguments().getString("data", "");

        if (!jsonData.equalsIgnoreCase("")) {
            List<String> listData = new Gson().fromJson(jsonData, new TypeToken<List<String>>() {
            }.getType());

            for (String d : listData) {
                data.add(d);
            }
            adapter.notifyDataSetInvalidated();
        }
    }

    @OnItemClick(R.id.list)
    public void onListClick(int position) {
        String item = data.get(position);
        onSelectedItem(item);
    }

    public abstract void onSelectedItem(String item);
}

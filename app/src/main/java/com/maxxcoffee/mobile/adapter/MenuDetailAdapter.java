package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.model.HistoryModel;
import com.maxxcoffee.mobile.model.MenuPriceModel;

import java.util.List;
import java.util.Map;

/**
 * Created by Rio Swarawan on 5/26/2016.
 */
public class MenuDetailAdapter extends BaseAdapter {

    private Context context;
    private List<MenuPriceModel> data;
    private LayoutInflater inflater;

    public MenuDetailAdapter(Context context, List<MenuPriceModel> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    class ViewHolder {
        TextView price;
        TextView size;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        ViewHolder holder = new ViewHolder();

        if (view == null) {
            view = inflater.inflate(R.layout.item_menu_detail, viewGroup, false);
            holder.size = (TextView) view.findViewById(R.id.size);
            holder.price = (TextView) view.findViewById(R.id.price);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        MenuPriceModel model = data.get(i);
        holder.size.setText(model.getSize());
        holder.price.setText(String.valueOf(model.getPrice()));
        return view;
    }
}

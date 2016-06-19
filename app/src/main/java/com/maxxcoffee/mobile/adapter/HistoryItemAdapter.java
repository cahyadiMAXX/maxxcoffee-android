package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.model.HistoryModel;
import com.maxxcoffee.mobile.model.StoreModel;

import java.util.List;

/**
 * Created by Rio Swarawan on 5/26/2016.
 */
public class HistoryItemAdapter extends BaseAdapter {

    private Context context;
    private List<HistoryModel> data;
    private LayoutInflater inflater;

    public HistoryItemAdapter(Context context, List<HistoryModel> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    class ViewHolder {
        TextView name;
        TextView location;
        TextView date;
        TextView balance;
        TextView point;
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
            view = inflater.inflate(R.layout.item_history, viewGroup, false);
            holder.name = (TextView) view.findViewById(R.id.card);
            holder.location = (TextView) view.findViewById(R.id.address);
            holder.date = (TextView) view.findViewById(R.id.contact);
            holder.balance = (TextView) view.findViewById(R.id.balance);
            holder.point = (TextView) view.findViewById(R.id.point);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        HistoryModel model = data.get(i);
        holder.name.setText(model.getCard());
        holder.location.setText(model.getLocation());
        holder.date.setText(model.getDate());
        holder.balance.setText(model.getBalance());
        holder.point.setText(model.getPoint());
        return view;
    }
}

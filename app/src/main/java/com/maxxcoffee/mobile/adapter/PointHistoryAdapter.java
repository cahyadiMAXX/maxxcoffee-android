package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.database.entity.HistoryEntity;
import com.maxxcoffee.mobile.util.Constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/26/2016.
 */
public class PointHistoryAdapter extends BaseAdapter {

    private Context context;
    private List<HistoryEntity> data;
    private DateFormat dateFormat;
    private LayoutInflater inflater;

    public PointHistoryAdapter(Context context, List<HistoryEntity> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.dateFormat = new SimpleDateFormat(Constant.DATEFORMAT_STRING_3);
    }

    class ViewHolder {
        TextView date;
        TextView store;
        TextView status;
        TextView balance;
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
    public View getView(int i, final View convertView, ViewGroup viewGroup) {
        View view = convertView;
        ViewHolder holder = new ViewHolder();

        if (view == null) {
            view = inflater.inflate(R.layout.item_transfer_history, viewGroup, false);
            holder.date = (TextView) view.findViewById(R.id.date);
            holder.store = (TextView) view.findViewById(R.id.store);
            holder.status = (TextView) view.findViewById(R.id.status);
            holder.balance = (TextView) view.findViewById(R.id.balance);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        try {
            HistoryEntity model = data.get(i);
            Date date = new SimpleDateFormat(Constant.DATEFORMAT_META).parse(model.getTime());
            holder.date.setText(dateFormat.format(date));
            holder.store.setText(model.getStore());
            holder.status.setText(model.getStatus());
            holder.balance.setText(String.valueOf(model.getAmount()));

            holder.store.setVisibility(model.getStore().equals("") ? View.GONE : View.VISIBLE);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }
}

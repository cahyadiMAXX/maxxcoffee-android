package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.model.StoreModel;

import java.util.List;

/**
 * Created by Rio Swarawan on 5/26/2016.
 */
public class StoreAdapter extends BaseAdapter {

    private Context context;
    private List<StoreModel> data;
    private LayoutInflater inflater;

    public StoreAdapter(Context context, List<StoreModel> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    class ViewHolder {
        TextView name;
        TextView address;
        TextView contact;
        TextView open;
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
            view = inflater.inflate(R.layout.item_store, viewGroup, false);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.address = (TextView) view.findViewById(R.id.address);
            holder.contact = (TextView) view.findViewById(R.id.contact);
            holder.open = (TextView) view.findViewById(R.id.open);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        StoreModel model = data.get(i);
        holder.name.setText(model.getName());
        holder.address.setText(model.getAddress());
        holder.contact.setText(model.getContact());
        holder.open.setText(model.getOpen());
        return view;
    }
}

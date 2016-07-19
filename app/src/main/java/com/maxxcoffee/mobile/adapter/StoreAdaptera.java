package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.database.entity.StoreEntity;

import java.util.List;

/**
 * Created by Rio Swarawan on 5/15/2016.
 */
public class StoreAdaptera extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<StoreEntity> data;
    private LayoutInflater inflater;

    public StoreAdaptera(Context context, List<StoreEntity> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_store, parent, false);
        return new BodyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StoreEntity model = data.get(position);
        BodyViewHolder body = (BodyViewHolder) holder;
        body.populate(model);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BodyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView address;
        TextView contact;
        TextView open;

        public BodyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            address = (TextView) view.findViewById(R.id.address);
            contact = (TextView) view.findViewById(R.id.contact);
            open = (TextView) view.findViewById(R.id.open);
        }

        public void populate(StoreEntity model) {
            name.setText(model.getName());
            address.setText(model.getAddress());
            contact.setText(model.getPhone());
            open.setText(model.getOpen());
        }
    }
}

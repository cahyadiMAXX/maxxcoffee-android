package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.database.entity.StoreEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rio Swarawan on 5/26/2016.
 */
public class StoreAdapter extends BaseAdapter {

    private String restroomCode = "restroom.png";
    private String seatCode = "seats.png";
    private String wifiCode = "wifi.png";
    private String mallCode = "mall.png";

    private Context context;
    private List<StoreEntity> data;
    private ArrayList<StoreEntity> arraylist;

    private LayoutInflater inflater;

    public StoreAdapter(Context context, List<StoreEntity> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<StoreEntity>();
        this.arraylist.addAll(data);
    }

    class ViewHolder {
        TextView name;
        TextView address;
        TextView contact;
        TextView open;
        ImageView restroom;
        ImageView seat;
        ImageView wifi;
        ImageView mall;
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
            view = inflater.inflate(R.layout.item_store, viewGroup, false);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.address = (TextView) view.findViewById(R.id.address);
            holder.contact = (TextView) view.findViewById(R.id.contact);
            holder.open = (TextView) view.findViewById(R.id.open);
            holder.restroom = (ImageView) view.findViewById(R.id.restroom);
            holder.seat = (ImageView) view.findViewById(R.id.seats);
            holder.wifi = (ImageView) view.findViewById(R.id.wifi);
            holder.mall = (ImageView) view.findViewById(R.id.mall);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        StoreEntity model = data.get(i);
        holder.name.setText(model.getName());
        holder.address.setText(model.getAddress());
        holder.contact.setText(model.getPhone());
        holder.open.setText(model.getOpen() + " - " + model.getClose());

        holder.restroom.setVisibility(model.getFeature_icon().contains(restroomCode) ? View.VISIBLE : View.INVISIBLE);
        holder.seat.setVisibility(model.getFeature_icon().contains(seatCode) ? View.VISIBLE : View.INVISIBLE);
        holder.wifi.setVisibility(model.getFeature_icon().contains(wifiCode) ? View.VISIBLE : View.INVISIBLE);
        holder.mall.setVisibility(model.getFeature_icon().contains(mallCode) ? View.VISIBLE : View.INVISIBLE);

        holder.restroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Has restroom.", Toast.LENGTH_LONG).show();
            }
        });
        holder.seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Has seats.", Toast.LENGTH_LONG).show();
            }
        });
        holder.wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Has Wifi", Toast.LENGTH_LONG).show();
            }
        });
        holder.mall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Store location is inside a Mall", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}

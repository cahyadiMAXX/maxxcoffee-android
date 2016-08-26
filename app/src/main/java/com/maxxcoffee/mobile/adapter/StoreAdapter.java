package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
public abstract class StoreAdapter extends BaseAdapter {

    private String restroomCode = "restroom.png";
    private String seatCode = "seats.png";
    private String wifiCode = "wifi.png";
    private String mallCode = "mall.png";

    private Context context;
    private List<StoreEntity> data;
    private ArrayList<StoreEntity> arraylist;
    private boolean isJarakVisible;
    private LayoutInflater inflater;

    public StoreAdapter(Context context, List<StoreEntity> data) {
        this(context, data, false);
    }

    public StoreAdapter(Context context, List<StoreEntity> data, boolean isJarakVisible) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<StoreEntity>();
        this.arraylist.addAll(data);
        this.isJarakVisible = isJarakVisible;
    }

    class ViewHolder {
        TextView name;
        TextView address;
        TextView contact;
        TextView open;
        TextView jarak;
        RelativeLayout layoutJarak;

        ImageView iconLocation;
        ImageView iconPhone;

        ImageView restroomJarak;
        ImageView seatJarak;
        ImageView wifiJarak;
        ImageView mallJarak;

        ImageView restroomJam;
        ImageView seatJam;
        ImageView wifiJam;
        ImageView mallJam;
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
            holder.jarak = (TextView) view.findViewById(R.id.jarak);
            holder.open = (TextView) view.findViewById(R.id.open);
            holder.layoutJarak = (RelativeLayout) view.findViewById(R.id.layout_jarak);

            holder.iconLocation = (ImageView) view.findViewById(R.id.icon_location);
            holder.iconPhone = (ImageView) view.findViewById(R.id.icon_phone);

            holder.restroomJarak = (ImageView) view.findViewById(R.id.restroom_jarak);
            holder.seatJarak = (ImageView) view.findViewById(R.id.seats_jarak);
            holder.wifiJarak = (ImageView) view.findViewById(R.id.wifi_jarak);
            holder.mallJarak = (ImageView) view.findViewById(R.id.mall_jarak);

            holder.restroomJam = (ImageView) view.findViewById(R.id.restroom_jam);
            holder.seatJam = (ImageView) view.findViewById(R.id.seats_jam);
            holder.wifiJam = (ImageView) view.findViewById(R.id.wifi_jam);
            holder.mallJam = (ImageView) view.findViewById(R.id.mall_jam);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        final StoreEntity model = data.get(i);
        String open = "", close = "";
        if (model.getOpen() != null) {
            String[] arrayOpen = model.getOpen().split(":");
            open = arrayOpen[0] + ":" + arrayOpen[1];
        }
        if (model.getClose() != null) {
            String[] arrayClose = model.getClose().split(":");
            close = arrayClose[0] + ":" + arrayClose[1];
        }
        holder.name.setText(model.getName());
        holder.address.setText(model.getAddress());
        holder.contact.setText(model.getPhone());
        holder.open.setText(open + " - " + close);
        holder.jarak.setText(model.getJarak());

        if (isJarakVisible) {
            holder.layoutJarak.setVisibility(View.VISIBLE);
            holder.restroomJarak.setVisibility(model.getFeature_icon().contains(restroomCode) ? View.VISIBLE : View.INVISIBLE);
            holder.seatJarak.setVisibility(model.getFeature_icon().contains(seatCode) ? View.VISIBLE : View.INVISIBLE);
            holder.wifiJarak.setVisibility(model.getFeature_icon().contains(wifiCode) ? View.VISIBLE : View.INVISIBLE);
            holder.mallJarak.setVisibility(model.getFeature_icon().contains(mallCode) ? View.VISIBLE : View.INVISIBLE);
        } else {
            holder.layoutJarak.setVisibility(View.GONE);
            holder.restroomJam.setVisibility(model.getFeature_icon().contains(restroomCode) ? View.VISIBLE : View.INVISIBLE);
            holder.seatJam.setVisibility(model.getFeature_icon().contains(seatCode) ? View.VISIBLE : View.INVISIBLE);
            holder.wifiJam.setVisibility(model.getFeature_icon().contains(wifiCode) ? View.VISIBLE : View.INVISIBLE);
            holder.mallJam.setVisibility(model.getFeature_icon().contains(mallCode) ? View.VISIBLE : View.INVISIBLE);
        }

        holder.restroomJarak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Has restroom.", Toast.LENGTH_LONG).show();
            }
        });
        holder.seatJarak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Has seats.", Toast.LENGTH_LONG).show();
            }
        });
        holder.wifiJarak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Has Wifi", Toast.LENGTH_LONG).show();
            }
        });
        holder.mallJarak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Store location is inside a Mall", Toast.LENGTH_LONG).show();
            }
        });

        holder.restroomJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Has restroom.", Toast.LENGTH_LONG).show();
            }
        });
        holder.seatJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Has seats.", Toast.LENGTH_LONG).show();
            }
        });
        holder.wifiJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Has Wifi", Toast.LENGTH_LONG).show();
            }
        });
        holder.mallJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Store location is inside a Mall", Toast.LENGTH_LONG).show();
            }
        });

        holder.iconLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOpenMap(model.getLatitude(), model.getLongitude(), model.getName());
            }
        });

        holder.iconPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCall(model.getPhone());
            }
        });

        return view;
    }

    protected abstract void onCall(String phone);

    protected abstract void onOpenMap(String lat, String lng, String place);
}

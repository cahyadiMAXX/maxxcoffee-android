package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.database.entity.EventEntity;

import java.util.List;

/**
 * Created by Rio Swarawan on 5/23/2016.
 */
public abstract class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<EventEntity> data;
    private LayoutInflater inflater;

    public EventAdapter(Context context, List<EventEntity> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_event, parent, false);
        return new BodyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EventEntity model = data.get(position);
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

        ImageView image;
        CardView layout;

        public BodyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            layout = (CardView) itemView.findViewById(R.id.card_view);
        }

        public void populate(final EventEntity model) {
            Glide.with(context).load(model.getLs_gambar())
                    .placeholder(context.getResources().getDrawable(R.drawable.ic_no_image))
                    //.centerCrop()
                    .fitCenter()
                    .crossFade().into(image);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCardClick(model);
                }
            });
        }
    }

    public abstract void onCardClick(EventEntity model);
}

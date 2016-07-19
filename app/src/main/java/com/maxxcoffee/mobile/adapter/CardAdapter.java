package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.model.CardModel;

import java.util.List;

/**
 * Created by Rio Swarawan on 5/23/2016.
 */
public abstract class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<CardEntity> data;
    private LayoutInflater inflater;

    public CardAdapter(Context context, List<CardEntity> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new BodyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CardEntity model = data.get(position);
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
        TextView name;
        TextView balance;
        TextView point;
        TextView bean;
        CardView card;

        public BodyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            balance = (TextView) itemView.findViewById(R.id.balance);
            point = (TextView) itemView.findViewById(R.id.point);
            bean = (TextView) itemView.findViewById(R.id.beans);
            card = (CardView) itemView.findViewById(R.id.card_view);
        }

        public void populate(final CardEntity model) {
            name.setText(model.getName());
            balance.setText("IDR " + model.getBalance());
            bean.setText(String.valueOf(model.getPoint()));
            Glide.with(context).load(model.getImage()).centerCrop().crossFade().into(image);
//            point.setText(String.valueOf(model.getPoint()));
//            image.setImageDrawable(context.getResources().getDrawable(model.getImage()));

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCardSelected(model);
                }
            });
        }
    }

    public abstract void onCardSelected(CardEntity model);

}

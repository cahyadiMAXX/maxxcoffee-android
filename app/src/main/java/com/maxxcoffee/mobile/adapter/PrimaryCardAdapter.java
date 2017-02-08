package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.model.CardModel;

import java.util.List;

/**
 * Created by Rio Swarawan on 5/23/2016.
 */
public abstract class PrimaryCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<CardModel> data;
    private LayoutInflater inflater;
    public int mSelectedItem = -1;

    public PrimaryCardAdapter(Context context, List<CardModel> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<CardModel> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_primary_card, parent, false);
        return new BodyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CardModel model = data.get(position);
        BodyViewHolder body = (BodyViewHolder) holder;
        body.checkBox.setChecked(position == mSelectedItem);
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

        TextView name, balance, point;
        LinearLayout layout;
        ImageView imageView;
        RadioButton checkBox;
        RelativeLayout card_3;

        public BodyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name_3);
            balance = (TextView) itemView.findViewById(R.id.balance_3);
            point = (TextView) itemView.findViewById(R.id.point_3);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_primary);
            imageView = (ImageView) itemView.findViewById(R.id.card_3);
            checkBox = (RadioButton) itemView.findViewById(R.id.checkbox_card);
            card_3 = (RelativeLayout) itemView.findViewById(R.id.relative);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, data.size());
                }
            };

            checkBox.setOnClickListener(clickListener);
            layout.setOnClickListener(clickListener);
            card_3.setOnClickListener(clickListener);
        }

        public void populate(final CardModel card) {
            //Toast.makeText(context, String.valueOf(card.getPrimary()), Toast.LENGTH_LONG).show();
            name.setText(card.getName());
            balance.setText(String.valueOf(card.getBalance()));
            point.setText(String.valueOf(card.getPoint()));

            try {
                Glide.with(context)
                        .load(card.getImage())
                        //.fitCenter()
                        //.crossFade()
                        .placeholder(R.drawable.ic_no_image)
                        .into(imageView);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public int getmSelectedItem(){
        return mSelectedItem;
    }

    public CardModel getItemSelected(){
        if(data.size() > 0){
            return data.get(mSelectedItem);
        }
        return null;
    }

    public abstract void onFaqClick(CardModel model);
}

package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.model.CardDesignModel;
import com.maxxcoffee.mobile.model.CardModel;

import java.util.List;

/**
 * Created by Rio Swarawan on 5/23/2016.
 */
public abstract class CardDesignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<CardDesignModel> data;
    private LayoutInflater inflater;
    public int mSelectedItem = -1;

    public CardDesignAdapter(Context context, List<CardDesignModel> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<CardDesignModel> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_card_design, parent, false);
        return new BodyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CardDesignModel model = data.get(position);
        BodyViewHolder body = (BodyViewHolder) holder;
        if(position == mSelectedItem){
            body.overlayCard.setVisibility(View.VISIBLE);
        }else{
            body.overlayCard.setVisibility(View.GONE);
        }
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
        FrameLayout overlayCard;

        public BodyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imagedesign);
            overlayCard = (FrameLayout) itemView.findViewById(R.id.overlaycard);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, data.size());
                    onCardDesignClick(data.get(mSelectedItem));
                }
            };

            imageView.setOnClickListener(clickListener);
        }

        public void populate(final CardDesignModel card) {

            try {
                Glide.with(context)
                        .load(card.getImageUrl())
                        //.fitCenter()
                        //.crossFade()
                        .dontAnimate()
                        .dontTransform()
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

    public CardDesignModel getItemSelected(){
        if(data.size() > 0){
            return data.get(mSelectedItem);
        }
        return null;
    }

    public abstract void onCardDesignClick(CardDesignModel model);
}

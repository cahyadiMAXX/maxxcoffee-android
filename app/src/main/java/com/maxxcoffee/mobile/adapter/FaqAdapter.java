package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.model.FaqModel;
import com.maxxcoffee.mobile.model.PromoModel;

import java.util.List;

/**
 * Created by Rio Swarawan on 5/23/2016.
 */
public abstract class FaqAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<FaqModel> data;
    private LayoutInflater inflater;

    public FaqAdapter(Context context, List<FaqModel> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_faq, parent, false);
        return new BodyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FaqModel model = data.get(position);
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
        RelativeLayout layout;

        public BodyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout);
        }

        public void populate(final FaqModel model) {
            name.setText(model.getTitle());
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFaqClick(model);
                }
            });
        }
    }

    public abstract void onFaqClick(FaqModel model);
}

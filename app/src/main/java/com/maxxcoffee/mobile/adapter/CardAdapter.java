package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.model.CardModel;
import com.maxxcoffee.mobile.task.DownloadImageTask;
import com.maxxcoffee.mobile.util.ImageSaver;
import com.maxxcoffee.mobile.util.Utils;

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
        CardView card;
        FrameLayout isPrimary;

        public BodyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            balance = (TextView) itemView.findViewById(R.id.balance);
            point = (TextView) itemView.findViewById(R.id.point);
            card = (CardView) itemView.findViewById(R.id.card_view);
            isPrimary = (FrameLayout) itemView.findViewById(R.id.isPrimary);
        }

        public void populate(final CardEntity model) {
            name.setText(model.getName());
            balance.setText("IDR " + model.getBalance());
            point.setText(String.valueOf(model.getPoint()));
            //Toast.makeText(context, String.valueOf(model.getIsPrimary()), Toast.LENGTH_LONG).show();
            if(model.getPrimary() == 1){
                isPrimary.setVisibility(View.VISIBLE);
            }else{
                isPrimary.setVisibility(View.GONE);
            }

            DownloadImageTask task = new DownloadImageTask(context) {
                @Override
                protected void onDownloadError() {
                    try {
                        Glide.with(context).load(model.getImage()).placeholder(R.drawable.ic_no_image).into(image);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                protected void onImageDownloaded(Bitmap bitmap) {
                    try{
                        /*Bitmap resizeImage = Utils.getResizedBitmap(bitmap, 0.95f);
                        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                        image.setImageDrawable(drawable);*/
                        ImageSaver imageSaver = new ImageSaver(context);
                        imageSaver.setFileName(model.getName() + "_on_adapter.png").
                                setDirectoryName("images").
                                save(bitmap);
                        imageSaver.setExternal(false);
                        loadImageFront(model.getName() + "_on_adapter.png");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };

            if(Utils.isConnected(context)){
                task.execute(model.getImage());
            }else{
                loadImageFront(model.getName() + "_on_adapter.png");
            }

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCardSelected(model);
                }
            });
        }

        public void loadImageFront(String filename){
            Bitmap bmt = new ImageSaver(context).
                    setFileName(filename).
                    setDirectoryName("images").
                    load();

            Drawable drawable = new BitmapDrawable(context.getResources(), bmt);
            image.setImageDrawable(drawable);
        }
    }

    public abstract void onCardSelected(CardEntity model);

}

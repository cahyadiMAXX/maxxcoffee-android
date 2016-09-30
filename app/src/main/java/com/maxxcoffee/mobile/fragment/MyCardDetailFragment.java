package com.maxxcoffee.mobile.fragment;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionMenu;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MoreDetailActivity;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.fragment.dialog.CardRenameDialog;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.fragment.dialog.QrCodeDialog;
import com.maxxcoffee.mobile.model.request.PrimaryCardRequestModel;
import com.maxxcoffee.mobile.model.request.RenameCardRequestModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.task.CardCGITask;
import com.maxxcoffee.mobile.task.CardDetailTask;
import com.maxxcoffee.mobile.task.DownloadImageTask;
import com.maxxcoffee.mobile.task.RenameCardTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.ImageSaver;
import com.maxxcoffee.mobile.util.OnSwipeTouchListener;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;
import com.maxxcoffee.mobile.widget.button.ButtonLatoBold;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class MyCardDetailFragment extends Fragment {

    private static int CARD_FRONT_STATE = 1000;
    private static int CARD_BACK_STATE = 999;

    @Bind(R.id.balance)
    TextView balance;
    @Bind(R.id.point)
    TextView point;
    @Bind(R.id.exp_date)
    TextView expDate;
    //@Bind(R.id.card_image)
    //ImageView cardImage;
    @Bind(R.id.beans_bubble)
    TextView beansBubble;
    @Bind(R.id.reward_achieved)
    TextView rewardAchieved;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.fab_menu)
    FloatingActionMenu fabMenu;
    @Bind(R.id.disable_layer)
    FrameLayout disableLayer;

    @Bind(R.id.qr)
    Button flipCard;
    @Bind(R.id.imageCardFront)
    ImageView imageCardFront;
    @Bind(R.id.imageCardBack)
    ImageView imageCardBack;

    private FormActivity activity;
    private CardController cardController;
    private String cardNumber;
    private String cardName;
    private String barcodeUrl;

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    @Bind(R.id.card_front)
    View mCardFrontLayout;
    @Bind(R.id.card_back)
    View mCardBackLayout;

    private int cardState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();

        cardController = new CardController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_card, container, false);

        ButterKnife.bind(this, view);

        activity.showRefreshButton(true);

        fetchingCard();

        try{
            activity.getRefresh().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchingData();
                }
            });
        }catch (Exception e){e.printStackTrace();}

        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabMenu.isOpened()) {
                    disableLayer.setVisibility(View.GONE);
                    fabMenu.close(true);
                } else {
                    disableLayer.setVisibility(View.VISIBLE);
                    fabMenu.open(true);
                }
            }
        });
        disableLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableLayer.setVisibility(View.GONE);
                fabMenu.close(true);
            }
        });

        //animasi card
        loadAnimations();
        changeCameraDistance();

        mCardBackLayout.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                flipCard(mCardBackLayout);
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                flipCard(mCardBackLayout);
            }

            @Override
            public void onClick() {
                super.onClick();
                flipCard(mCardBackLayout);
            }
        });

        mCardFrontLayout.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                flipCard(mCardFrontLayout);
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                flipCard(mCardFrontLayout);
            }

            @Override
            public void onClick() {
                super.onClick();
                flipCard(mCardFrontLayout);
            }
        });

        return view;
    }

    private void fetchingData() {
        //ngambil number
        if(!Utils.isConnected(activity)){
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            return;
        }
        final String cardnumber = getArguments().getString("card-number", "-1");
        PrimaryCardRequestModel body = new PrimaryCardRequestModel();
        body.setCard_number(cardnumber);

        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        CardDetailTask task = new CardDetailTask(activity) {
            @Override
            public void onSuccess(List<CardItemResponseModel> responseModel) {
                //Log.d("responseModel", responseModel.toString());
                try{
                    CardItemResponseModel card  =  responseModel.get(0);

                    DateFormat mDateFormat = new SimpleDateFormat(Constant.DATEFORMAT_STRING);
                    Date mExpDate = new SimpleDateFormat(Constant.DATEFORMAT_META).parse(card.getExpired_date());

                    int mRewardAchieve = card.getBeans() / 10;
                    int mRewardToGo = 10 - (card.getBeans() % 10);

                    activity.setTitle(card.getCard_name());
                    name.setText(card.getCard_name());
                    balance.setText("IDR " + card.getBalance());
                    point.setText(card.getBeans() + " beans");
                    expDate.setText(mDateFormat.format(mExpDate));
                    beansBubble.setText(String.valueOf(mRewardToGo));
                    rewardAchieved.setText(String.valueOf(mRewardAchieve));
                    barcodeUrl = card.getBarcode();
                    loadImages(card.getCard_image(),card.getBarcode(), card.getCard_name());
                    //save ?

                    CardEntity entity = new CardEntity();
                    entity.setId(card.getId_card());
                    entity.setName(card.getCard_name());
                    entity.setNumber(card.getCard_number());
                    entity.setImage(card.getCard_image());
                    entity.setDistribution_id(card.getDistribution_id());
                    entity.setCard_pin(card.getCard_pin());
                    entity.setBalance(card.getBalance());
                    entity.setPoint(card.getBeans());
                    entity.setBarcode(card.getBarcode());
                    entity.setExpired_date(card.getExpired_date());
                    entity.setPrimary(card.getPrimary());

                    cardController.insert(entity);
                }catch (Exception e){
                    e.printStackTrace();
                }

                //fetchingCard();
                progress.dismissAllowingStateLoss();
            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
            }
        };
        task.execute(body);
    }

    private void fetchingCard() {
        final String cardId = getArguments().getString("card-number", "-1");

        final CardEntity card = cardController.getCardByCardNumber(cardId);
        if (card != null) {
            try {
                cardNumber = card.getNumber();
                cardName = card.getName();

                DateFormat mDateFormat = new SimpleDateFormat(Constant.DATEFORMAT_STRING);
                Date mExpDate = new SimpleDateFormat(Constant.DATEFORMAT_META).parse(card.getExpired_date());

                int mBalance = card.getBalance();
                int mPoint = card.getPoint();
                int mRewardAchieve = mPoint / 10;
                int mRewardToGo = 10 - (mPoint % 10);

                activity.setTitle(card.getName());
                name.setText(card.getName());
                balance.setText("IDR " + mBalance);
                point.setText(mPoint + " beans");
                expDate.setText(mDateFormat.format(mExpDate));
                beansBubble.setText(String.valueOf(mRewardToGo));
                rewardAchieved.setText(String.valueOf(mRewardAchieve));
                barcodeUrl = card.getBarcode();
                loadImages(card.getImage(), card.getBarcode(), card.getName());
                /**/

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    //depan image, belakang barcode
    private void loadImages(String image, final String barcode, final String name){
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        final DownloadImageTask barcodeTask = new DownloadImageTask(getContext()) {
            @Override
            protected void onDownloadError() {
                progress.dismissAllowingStateLoss();
                try{
                    Glide.with(activity).load("").placeholder(R.drawable.ic_no_image).into(imageCardBack);
                    loadImageBack(name + "_imageBack.png");
                }catch (Exception e){e.printStackTrace();}
            }

            @Override
            protected void onImageDownloaded(Bitmap bitmap) {
                progress.dismissAllowingStateLoss();
                try{
                            /*Bitmap resizeImage = Utils.getResizedBitmap(bitmap, 0.95f);
                            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                            imageCardBack.setImageDrawable(drawable);*/
                    ImageSaver imageSaver = new ImageSaver(activity);
                    imageSaver.setFileName(name + "_imageBack.png").
                            setDirectoryName("images").
                            save(bitmap);
                    imageSaver.setExternal(false);
                    loadImageBack(name + "_imageBack.png");
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };

        //klo ada file, pake file saja
        final DownloadImageTask task = new DownloadImageTask(activity) {
            @Override
            protected void onDownloadError() {
                try{
                    barcodeTask.execute(barcode);
                    Glide.with(activity).load("").placeholder(R.drawable.ic_no_image).into(imageCardFront);
                    loadImageFront(name + "_imageFront.png");
                }catch (Exception e){e.printStackTrace();}
            }

            @Override
            protected void onImageDownloaded(Bitmap bitmap) {
                try{
                    barcodeTask.execute(barcode);
                            /*Bitmap resizeImage = Utils.getResizedBitmap(bitmap, 0.95f);
                            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                            imageCardFront.setImageDrawable(drawable);*/
                    ImageSaver imageSaver = new ImageSaver(activity);
                    imageSaver.setFileName(name + "_imageFront.png").
                            setDirectoryName("images").
                            save(bitmap);
                    imageSaver.setExternal(false);
                    loadImageFront(name + "_imageFront.png");
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };

        if(Utils.isConnected(activity)){
            task.execute(image);
        }else{
            try{
                progress.dismissAllowingStateLoss();
                loadImageFront(name + "_imageFront.png");
                loadImageBack(name + "_imageBack.png");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void loadImageFront(String filename){
        Bitmap bmt = new ImageSaver(activity).
                setFileName(filename).
                setDirectoryName("images").
                load();

        Drawable drawable = new BitmapDrawable(getResources(), bmt);
        imageCardFront.setImageDrawable(drawable);
    }

    public void loadImageBack(String filename){
        Bitmap bmt = new ImageSaver(activity).
                setFileName(filename).
                setDirectoryName("images").
                load();

        Drawable drawable = new BitmapDrawable(getResources(), bmt);
        imageCardBack.setImageDrawable(drawable);
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.in_animation);
    }

    public void flipCard(View view) {
        if (!mIsBackVisible) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;
            flipCard.setText("Hide qr code");
        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
            flipCard.setText("qr code");
        }
    }

    @OnClick(R.id.topup)
    public void onTopupClick() {
        Toast.makeText(activity, "This feature not supported yet", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.layout_name)
    public void onLayoutNameClick() {
        CardRenameDialog dialog = new CardRenameDialog(name.getText().toString()) {
            @Override
            protected void onRename(String n) {
                String name = n.equals("") ? "My MAXX Card" : n;
                renameCard(name);
                dismiss();
            }
        };
        dialog.show(getFragmentManager(), null);
    }

    private void renameCard(final String name) {
        String cardId = getArguments().getString("card-id", "-1");

        RenameCardRequestModel body = new RenameCardRequestModel();
        body.setId_card(cardId);
        body.setNew_name(name);

        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        RenameCardTask task = new RenameCardTask(activity) {
            @Override
            public void onSuccess() {
                progress.dismissAllowingStateLoss();
                MyCardDetailFragment.this.name.setText(name);
                activity.setTitle(name);
                fetchingData();
            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
            }
        };
        task.execute(body);
    }

    @OnClick(R.id.history)
    public void onHistoryClick() {
        Bundle bundle = new Bundle();
        bundle.putString("card-number", cardNumber);
        bundle.putString("card-name", cardName);

        Intent in = new Intent(activity, MoreDetailActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in.putExtra("content", MoreDetailActivity.HISTORY_DETAIL);
        in.putExtras(bundle);
        startActivity(in);
        //activity.switchFragment(MoreDetailActivity.HISTORY_DETAIL, bundle);
    }

    //button click
    @OnClick(R.id.qr)
    public void onShowQrCode() {
        flipCard(mCardBackLayout);
        /*disableLayer.setVisibility(View.GONE);
        fabMenu.close(true);
        // show dialog qr code
        QrCodeDialog qrDialog = new QrCodeDialog();

        Bundle bundle = new Bundle();
        bundle.putString("qr", barcodeUrl);

        qrDialog.setArguments(bundle);
        qrDialog.show(getFragmentManager(), null);*/

    }
}

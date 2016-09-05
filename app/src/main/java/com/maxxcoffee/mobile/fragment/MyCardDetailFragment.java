package com.maxxcoffee.mobile.fragment;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.fragment.dialog.CardRenameDialog;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.fragment.dialog.QrCodeDialog;
import com.maxxcoffee.mobile.model.request.RenameCardRequestModel;
import com.maxxcoffee.mobile.task.DownloadImageTask;
import com.maxxcoffee.mobile.task.RenameCardTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.OnSwipeTouchListener;
import com.maxxcoffee.mobile.util.Utils;
import com.maxxcoffee.mobile.widget.button.ButtonLatoBold;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        activity.showRefreshButton(true);
        cardController = new CardController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_card, container, false);

        ButterKnife.bind(this, view);

        fetchingCard();

        activity.getRefresh().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicke boss", Toast.LENGTH_LONG).show();
                fetchingCard();
            }
        });

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
        });

        return view;
    }

    private void fetchingCard() {
        final String cardId = getArguments().getString("card-id", "-1");

        final CardEntity card = cardController.getCardById(cardId);
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

                //klo ada file, pake file saja
                DownloadImageTask task = new DownloadImageTask(activity) {
                    @Override
                    protected void onDownloadError() {
                        //Glide.with(activity).load("").placeholder(R.drawable.ic_no_image).into(cardImage);
                        Glide.with(activity).load("").placeholder(R.drawable.ic_no_image).into(imageCardFront);
                    }

                    @Override
                    protected void onImageDownloaded(Bitmap bitmap) {
                        Bitmap resizeImage = Utils.getResizedBitmap(bitmap, 0.95f);
                        Drawable drawable = new BitmapDrawable(getResources(), resizeImage);
                        //cardImage.setImageDrawable(drawable);
                        imageCardFront.setImageDrawable(drawable);
                    }
                };
                task.execute(card.getImage());

                DownloadImageTask barcodeTask = new DownloadImageTask(getContext()) {
                    @Override
                    protected void onDownloadError() {
                        Glide.with(getContext()).load("").placeholder(R.drawable.ic_no_image).into(imageCardBack);
                    }

                    @Override
                    protected void onImageDownloaded(Bitmap bitmap) {
                        Bitmap resizeImage = Utils.getResizedBitmap(bitmap, 0.95f);
                        Drawable drawable = new BitmapDrawable(getResources(), resizeImage);
                        imageCardBack.setImageDrawable(drawable);
                    }
                };
                barcodeTask.execute(card.getBarcode());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean fileExistance(String fname){
        File file = getActivity().getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    public void saveImageBitmap(Bitmap bmp, String filename){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            Toast.makeText(getActivity(), "Saved Image", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.anim.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.anim.in_animation);
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

        activity.switchFragment(FormActivity.HISTORY_DETAIL, bundle);
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

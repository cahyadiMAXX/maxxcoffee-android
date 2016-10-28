package com.maxxcoffee.mobile.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.fragment.dialog.OkDialog;
import com.maxxcoffee.mobile.fragment.dialog.OptionDialog;
import com.maxxcoffee.mobile.model.request.DeleteCardRequestModel;
import com.maxxcoffee.mobile.task.DeleteCardTask;
import com.maxxcoffee.mobile.task.DownloadImageTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class DeleteCardFragment extends Fragment {

//    @Bind(R.id.radio_group)
//    RadioGroup radioGroup;

    @Bind(R.id.card_1)
    ImageView card1;
    @Bind(R.id.layout_card_1)
    LinearLayout layoutCard1;
    @Bind(R.id.checkbox_card_1)
    RadioButton checkBoxCarad1;
    @Bind(R.id.name_1)
    TextView name1;
    @Bind(R.id.balance_1)
    TextView balance1;
    @Bind(R.id.point_1)
    TextView point1;

    @Bind(R.id.card_2)
    ImageView card2;
    @Bind(R.id.layout_card_2)
    LinearLayout layoutCard2;
    @Bind(R.id.checkbox_card_2)
    RadioButton checkBoxCarad2;
    @Bind(R.id.name_2)
    TextView name2;
    @Bind(R.id.balance_2)
    TextView balance2;
    @Bind(R.id.point_2)
    TextView point2;

    @Bind(R.id.card_3)
    ImageView card3;
    @Bind(R.id.layout_card_3)
    LinearLayout layoutCard3;
    @Bind(R.id.checkbox_card_3)
    RadioButton checkBoxCarad3;
    @Bind(R.id.name_3)
    TextView name3;
    @Bind(R.id.balance_3)
    TextView balance3;
    @Bind(R.id.point_3)
    TextView point3;

    @Bind(R.id.delete)
    Button deleteButton;

    private FormActivity activity;
    private CardController cardController;
    private String cardNumber1;
    private String cardNumber2;
    private String cardNumber3;
    //    private boolean card1Selected;
//    private boolean card2Selected;
//    private boolean card3Selected;
    private String selectedCard = "";
    private String selectedPoint;
    private int iteration = 0;
    private List<String> cardSelectedCollection;

    public DeleteCardFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
        cardController = new CardController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_card, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Delete Card");

        fetchingCard();

        layoutCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card1Selected();
            }
        });
        layoutCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card2Selected();
            }
        });
        layoutCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card3Selected();
            }
        });
        checkBoxCarad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card1Selected();
            }
        });
        checkBoxCarad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card2Selected();
            }
        });
        checkBoxCarad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card3Selected();
            }
        });

        return view;
    }

    private void card1Selected() {
        selectedCard = cardNumber1;
        selectedPoint = balance1.getText().toString();

        checkBoxCarad1.setChecked(true);
        checkBoxCarad2.setChecked(false);
        checkBoxCarad3.setChecked(false);
    }

    private void card2Selected() {
        selectedCard = cardNumber2;
        selectedPoint = balance2.getText().toString();

        checkBoxCarad1.setChecked(false);
        checkBoxCarad2.setChecked(true);
        checkBoxCarad3.setChecked(false);
    }

    private void card3Selected() {
        selectedCard = cardNumber3;
        selectedPoint = balance3.getText().toString();

        checkBoxCarad1.setChecked(false);
        checkBoxCarad2.setChecked(false);
        checkBoxCarad3.setChecked(true);
    }

    private void fetchingCard() {
        List<CardEntity> cards = cardController.getCards();

        for (int position = 0; position < cards.size(); position++) {
            CardEntity card = cards.get(position);
            if(card.getVirtual_card() == 0){
                if (position == 0) {
                    cardNumber1 = card.getNumber();
                    name1.setText(card.getName());
                    balance1.setText(String.valueOf(card.getBalance()));
                    point1.setText(String.valueOf(card.getPoint()));
                    DownloadImageTask task = new DownloadImageTask(activity) {
                        @Override
                        protected void onDownloadError() {
                            Glide.with(activity).load("").placeholder(R.drawable.ic_no_image).into(card1);
                        }

                        @Override
                        protected void onImageDownloaded(Bitmap bitmap) {
                            Bitmap resizeImage = Utils.getResizedBitmap(bitmap, 0.95f);
                            Drawable drawable = new BitmapDrawable(getResources(), resizeImage);
                            card1.setImageDrawable(drawable);
                        }
                    };
                    task.execute(card.getImage());
                    layoutCard1.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    cardNumber2 = card.getNumber();
                    name2.setText(card.getName());
                    balance2.setText(String.valueOf(card.getBalance()));
                    point2.setText(String.valueOf(card.getPoint()));
                    DownloadImageTask task = new DownloadImageTask(activity) {
                        @Override
                        protected void onDownloadError() {
                            Glide.with(activity).load("").placeholder(R.drawable.ic_no_image).into(card2);
                        }

                        @Override
                        protected void onImageDownloaded(Bitmap bitmap) {
                            Bitmap resizeImage = Utils.getResizedBitmap(bitmap, 0.95f);
                            Drawable drawable = new BitmapDrawable(getResources(), resizeImage);
                            card2.setImageDrawable(drawable);
                        }
                    };
                    task.execute(card.getImage());
                    layoutCard2.setVisibility(View.VISIBLE);
                } else if (position == 2) {
                    cardNumber3 = card.getNumber();
                    name3.setText(card.getName());
                    balance3.setText(String.valueOf(card.getBalance()));
                    point3.setText(String.valueOf(card.getPoint()));
                    DownloadImageTask task = new DownloadImageTask(activity) {
                        @Override
                        protected void onDownloadError() {
                            Glide.with(activity).load("").placeholder(R.drawable.ic_no_image).into(card3);
                        }

                        @Override
                        protected void onImageDownloaded(Bitmap bitmap) {
                            Bitmap resizeImage = Utils.getResizedBitmap(bitmap, 0.95f);
                            Drawable drawable = new BitmapDrawable(getResources(), resizeImage);
                            card3.setImageDrawable(drawable);
                        }
                    };
                    task.execute(card.getImage());
                    layoutCard3.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @OnClick(R.id.delete)
    public void onDeleteClick() {
        if (!isAnyCardSelected())
            return;

        if (!selectedPoint.equals("0")) {
            Bundle bundle = new Bundle();
            bundle.putString("content", "You have balance in your card. Please transfer it to another card before deleting this card.");

            OkDialog optionDialog = new OkDialog() {
                @Override
                protected void onOk() {
                    PreferenceManager.putBool(activity, Constant.PREFERENCE_ROUTE_TO_TRANSFER_BALANCE, true);
                    activity.onBackClick();
                }
            };
            optionDialog.setArguments(bundle);
            optionDialog.show(getFragmentManager(), null);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("content", "Delete card now?");
            bundle.putString("default", OptionDialog.CANCEL);

            OptionDialog optionDialog = new OptionDialog() {
                @Override
                protected void onOk() {
                    dismiss();
                    try {
                        if(Utils.isConnected(activity)){
                            deleteNow();
                        }else {
                            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                protected void onCancel() {
                    dismiss();
                }
            };
            optionDialog.setArguments(bundle);
            optionDialog.show(getFragmentManager(), null);
        }
    }

    private void deleteNow() {
        if(!Utils.isConnected(activity)){
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            return;
        }

        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        DeleteCardRequestModel body = new DeleteCardRequestModel();
        body.setCard_number(selectedCard);

        DeleteCardTask task = new DeleteCardTask(activity) {
            @Override
            public void onSuccess() {
                PreferenceManager.putBool(activity, Constant.PREFERENCE_CARD_IS_LOADING, false);
                progress.dismissAllowingStateLoss();
                activity.onBackClick();
            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
                Toast.makeText(activity, "Failed to delete card", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute(body);
    }

    public boolean isAnyCardSelected() {
        if (selectedCard.equals("")) {
            Toast.makeText(activity, "Please select your card to delete", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

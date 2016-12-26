package com.maxxcoffee.mobile.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.activity.VerificationActivity;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.fragment.dialog.LostCardDialog;
import com.maxxcoffee.mobile.fragment.dialog.OkDialog;
import com.maxxcoffee.mobile.fragment.dialog.OptionDialog;
import com.maxxcoffee.mobile.model.CardModel;
import com.maxxcoffee.mobile.model.request.TransferBalanceRequestModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.task.CardBalanceTask;
import com.maxxcoffee.mobile.task.CardTask;
import com.maxxcoffee.mobile.task.DownloadImageTask;
import com.maxxcoffee.mobile.task.TransferBalanceTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class TransferBalanceFragment extends Fragment {

    @Bind(R.id.card_name_source)
    TextView cardSource;
    @Bind(R.id.balance_source)
    TextView balanceSource;
    @Bind(R.id.beans_source)
    TextView beansSource;
    @Bind(R.id.exp_date_source)
    TextView expDateSource;
    @Bind(R.id.image_card_source)
    ImageView imageSource;

    @Bind(R.id.card_name_target)
    TextView cardTarget;
    @Bind(R.id.balance_target)
    TextView balanceTarget;
    @Bind(R.id.beans_target)
    TextView beansTarget;
    @Bind(R.id.exp_date_target)
    TextView expDateTarget;
    @Bind(R.id.image_card_target)
    ImageView imageTarget;
    @Bind(R.id.empty)
    TextView empty;
    @Bind(R.id.transfer_layout)
    LinearLayout transferLayout;

    private MainActivity activity;
    private CardController cardController;
    private List<CardModel> data;
    private CardModel selectedSource;
    private CardModel selectedTarget;
    private Integer selectedSourceId = -999;
    private Integer selectedTargetId = -999;

    Dialog loading;

    public TransferBalanceFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);

        cardController = new CardController(activity);
        data = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance_transfer, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Balance Transfer");

        loading = new Dialog(getActivity());
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);

        if(Utils.isConnected(activity)){
            fetchingData(false);
        }else{
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void fetchingData(final boolean autoSet) {
        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);*/
        loading.show();

        CardBalanceTask task = new CardBalanceTask(activity) {
            @Override
            public void onSuccess(List<CardItemResponseModel> responseModel) {
                int size = responseModel.size();
                if (size == 1) {
                    transferLayout.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                    empty.setText("You only have 1 card");
                } else {
                    transferLayout.setVisibility(View.VISIBLE);
                    for (CardItemResponseModel card : responseModel) {
                        CardEntity entity = new CardEntity();
                        entity.setId(card.getId_card());
                        entity.setName(card.getCard_name());
                        entity.setNumber(card.getCard_number());
                        entity.setImage(card.getCard_image());
                        entity.setDistribution_id(card.getDistribution_id());
                        entity.setCard_pin(card.getCard_pin());
                        entity.setBalance(card.getBalance());
                        entity.setPoint(card.getBeans());
                        entity.setExpired_date(card.getExpired_date());

                        cardController.insert(entity);
                    }
                    getLocalCard(autoSet);
                }
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
            }

            @Override
            public void onFailed() {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
                transferLayout.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
                empty.setText("You do not have any connected card. \n\nPlease add card.");
            }
        };
        task.execute();
    }

    private void getLocalCard(boolean autoSet) {
        List<CardEntity> cards = cardController.getCards();

        for (CardEntity card : cards) {
            CardModel model = new CardModel();
            model.setId(card.getId());
            model.setName(card.getName());
            model.setNumber(card.getNumber());
            model.setImage(card.getImage());
            model.setDistribution_id(card.getDistribution_id());
            model.setCard_pin(card.getCard_pin());
            model.setBalance(card.getBalance());
            model.setPoint(card.getPoint());
            model.setExpired_date(card.getExpired_date());

            data.add(model);
        }

        if (autoSet) {
            CardEntity sourceEntity = cardController.getCardByDist(String.valueOf(selectedSource.getId()));
            CardEntity targetEntity = cardController.getCardByDist(String.valueOf(selectedTarget.getId()));

            if (sourceEntity != null) {
                selectedSource.setBalance(sourceEntity.getBalance());
                setCardSource(selectedSource);
            }
            if (targetEntity != null) {
                selectedTarget.setBalance(targetEntity.getBalance());
                setCardTarget(selectedTarget);
            }
        }
    }

    private boolean isFormValid() {
        if (selectedSource == null) {
            Toast.makeText(activity, "Please select your source card", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedTarget == null) {
            Toast.makeText(activity, "Please select your destination card", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedTarget == selectedSource) {
            Toast.makeText(activity, "Cannot transfer to the same card", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.submit)
    public void onSubmitClick() {
        if (!isFormValid())
            return;

        if(!Utils.isConnected(activity)){
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
        }

        String content = "You will transfer all balance from " + selectedSource.getName().toUpperCase() + " to " + selectedTarget.getName().toUpperCase();

        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        bundle.putString("default", OptionDialog.CANCEL);

        OptionDialog optionDialog = new OptionDialog() {
            @Override
            protected void onOk() {
                dismiss();
                transferNow();
            }

            @Override
            protected void onCancel() {
                dismiss();
            }
        };
        optionDialog.setArguments(bundle);
        optionDialog.show(getFragmentManager(), null);
    }

    private void transferNow() {
        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);*/

        final Dialog progress;
        progress = new Dialog(getActivity());
        progress.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progress.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        progress.setContentView(R.layout.dialog_loading);
        progress.setCancelable(false);
        progress.show();

        TransferBalanceRequestModel body = new TransferBalanceRequestModel();
        body.setSource_dist_id(selectedSource.getDistribution_id());
        body.setDestination_dist_id(selectedTarget.getDistribution_id());

        TransferBalanceTask task = new TransferBalanceTask(activity) {
            @Override
            public void onSuccess() {
                //progress.dismissAllowingStateLoss();
                if (progress.isShowing())progress.dismiss();
                showSuccessDialog(true);
            }

            @Override
            public void onFailed() {
                //progress.dismissAllowingStateLoss();
                if (progress.isShowing())progress.dismiss();
                showSuccessDialog(false);
            }
        };

        task.execute(body);
    }

    private void showSuccessDialog(boolean success) {
        String successString = "You have successfully transferred from " + selectedSource.getName().toUpperCase() + " to " + selectedTarget.getName().toUpperCase();
        String failedString = "Fail to transfer from " + selectedSource.getName().toUpperCase() + " to " + selectedTarget.getName().toUpperCase();

        Bundle bundle = new Bundle();
        bundle.putString("content", success ? successString : failedString);

        /*OkDialog optionDialog = new OkDialog() {
            @Override
            protected void onOk() {
                dismiss();
                fetchingData(true);
                activity.switchFragment(MainActivity.MY_CARD);
            }
        };
        optionDialog.setArguments(bundle);
        optionDialog.show(getFragmentManager(), null);*/
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_ok, null);
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity()).setView(layout);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView cont = (TextView) layout.findViewById(R.id.content);
        Button buttonSerial = (Button) layout.findViewById(R.id.ok);

        cont.setText(success ? successString : failedString);
        buttonSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @OnClick(R.id.source_layout)
    public void onSourceClick() {
        if (data.size() == 0) {
            Toast.makeText(activity, "You do not have any connected card.\n\nPlease add card.", Toast.LENGTH_SHORT).show();
            return;
        }

        LostCardDialog lostCardDialog = new LostCardDialog() {
            @Override
            protected void onOk(Integer index) {
                selectedSourceId = index;
                if (index == CARD_1) {
                    setCardSource(data.get(0));
                } else if (index == CARD_2) {
                    setCardSource(data.get(1));
                } else if (index == CARD_3) {
                    setCardSource(data.get(2));
                }
                dismiss();
            }

            @Override
            protected void onCancel() {
                dismiss();
            }
        };

        String cardString = new Gson().toJson(data);

        Bundle bundle = new Bundle();
        bundle.putInt("selected-report", LostCardDialog.CARD_1);
        bundle.putInt("selected-another-card", selectedTargetId);
        bundle.putString("cards", cardString);

        lostCardDialog.setArguments(bundle);
        lostCardDialog.show(getFragmentManager(), null);
    }

    @OnClick(R.id.target_layout)
    public void onTargetClick() {
        if (data.size() == 0) {
            Toast.makeText(activity, "You do not have any connected card. \n\nPlease add card.", Toast.LENGTH_SHORT).show();
            return;
        }

        LostCardDialog lostCardDialog = new LostCardDialog() {
            @Override
            protected void onOk(Integer index) {
                selectedTargetId = index;
                if (index == CARD_1) {
                    setCardTarget(data.get(0));
                } else if (index == CARD_2) {
                    setCardTarget(data.get(1));
                } else if (index == CARD_3) {
                    setCardTarget(data.get(2));
                }
                dismiss();
            }

            @Override
            protected void onCancel() {
                dismiss();
            }
        };

        String cardString = new Gson().toJson(data);

        Bundle bundle = new Bundle();
        bundle.putInt("selected-report", LostCardDialog.CARD_1);
        bundle.putInt("selected-another-card", selectedSourceId);

        bundle.putString("cards", cardString);

        lostCardDialog.setArguments(bundle);
        lostCardDialog.show(getFragmentManager(), null);
    }

    private void setCardSource(CardModel cardModel) {
        Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATEFORMAT_META);
            d = sdf.parse(cardModel.getExpired_date());
            sdf.applyPattern(Constant.DATEFORMAT_POST);

            selectedSource = cardModel;
            cardSource.setText(cardModel.getName());
            balanceSource.setText("IDR " + cardModel.getBalance());
            beansSource.setText(String.valueOf(cardModel.getPoint()));
            expDateSource.setText(sdf.format(d));
            DownloadImageTask task = new DownloadImageTask(activity) {
                @Override
                protected void onDownloadError() {
                    Glide.with(activity).load("").placeholder(R.drawable.ic_no_image).into(imageSource);
                }

                @Override
                protected void onImageDownloaded(Bitmap bitmap) {
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    imageSource.setImageDrawable(drawable);
                }
            };
            task.execute(cardModel.getImage());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setCardTarget(CardModel cardModel) {
        Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATEFORMAT_META);
            d = sdf.parse(cardModel.getExpired_date());
            sdf.applyPattern(Constant.DATEFORMAT_POST);

            selectedTarget = cardModel;
            cardTarget.setText(cardModel.getName());
            balanceTarget.setText("IDR " + cardModel.getBalance());
            beansTarget.setText(String.valueOf(cardModel.getPoint()));
            expDateTarget.setText(sdf.format(d));
            DownloadImageTask task = new DownloadImageTask(activity) {
                @Override
                protected void onDownloadError() {
                    Glide.with(activity).load("").placeholder(R.drawable.ic_no_image).into(imageTarget);
                }

                @Override
                protected void onImageDownloaded(Bitmap bitmap) {
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    imageTarget.setImageDrawable(drawable);
                }
            };
            task.execute(cardModel.getImage());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

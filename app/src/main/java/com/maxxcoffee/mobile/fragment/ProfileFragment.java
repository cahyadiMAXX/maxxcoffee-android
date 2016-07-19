package com.maxxcoffee.mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.controller.ProfileController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.database.entity.ProfileEntity;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.ProfileItemResponseModel;
import com.maxxcoffee.mobile.model.response.ProfileResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.widget.TBaseProgress;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.maxxcoffee.mobile.task.ProfileTask;

import java.util.List;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class ProfileFragment extends Fragment {

    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.email)
    TextView email;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.balance)
    TextView balance;
    @Bind(R.id.beans)
    TextView beans;
    @Bind(R.id.card)
    TextView card;

    private MainActivity activity;
    private ProfileController profileController;
    private CardController cardController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);
        profileController = new ProfileController(activity);
        cardController = new CardController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Profile");

        getLocalProfile();
        fetchingData();

        return view;
    }

    private void getLocalProfile() {
        ProfileEntity profile = profileController.getProfile();
        List<CardEntity> cards = cardController.getCards();
        if (profile != null) {
            name.setText(profile.getName());
            email.setText(profile.getEmail());
            phone.setText(profile.getPhone());
            balance.setText("IDR " + String.valueOf(profile.getBalance()));
            beans.setText(String.valueOf(profile.getPoint()));
            card.setText(String.valueOf(cards.size()));
        }
    }

    private void fetchingData() {
        final TBaseProgress progress = new TBaseProgress(activity);
        progress.show();

        ProfileTask task = new ProfileTask(activity) {
            @Override
            public void onSuccess(ProfileResponseModel profile) {
                ProfileItemResponseModel profileItem = profile.getUser_profile().get(0);

                if (profileItem != null) {
                    ProfileEntity profileEntity = new ProfileEntity();
                    profileEntity.setId(profileItem.getId_user());
                    profileEntity.setName(profileItem.getNama_user());
                    profileEntity.setEmail(profileItem.getEmail());
                    profileEntity.setCity(profileItem.getKota_user());
                    profileEntity.setPhone(profileItem.getMobile_phone_user());
                    profileEntity.setBirthday(profileItem.getTanggal_lahir());
                    profileEntity.setGender(profileItem.getGender());
                    profileEntity.setOccupation(profileItem.getOccupation());
                    profileEntity.setImage(profileItem.getGambar());
                    profileEntity.setPoint(Integer.parseInt(profile.getTotal_point()));
                    profileEntity.setBalance(Integer.parseInt(profile.getTotal_balance()));

                    profileController.insert(profileEntity);

                    List<CardItemResponseModel> cards = profile.getCards();
                    if (cards.size() > 0) {
                        int totalBalance = 0;
                        int totalPoint = 0;

                        for (CardItemResponseModel card : cards) {
                            CardEntity cardEntity = new CardEntity();
                            cardEntity.setId(card.getId_card());
                            cardEntity.setName(card.getCard_name());
                            cardEntity.setNumber(card.getCard_number());
                            cardEntity.setImage(card.getCard_image());
                            cardEntity.setDistribution_id(card.getDistribution_id());
                            cardEntity.setCard_pin(card.getCard_pin());
                            cardEntity.setBalance(card.getBalance());
                            cardEntity.setPoint(card.getPoint());
                            cardEntity.setExpired_date(card.getExpired_date());

                            totalBalance += card.getBalance();
                            totalPoint += card.getPoint();

                            cardController.insert(cardEntity);
                        }
                        PreferenceManager.putString(activity, Constant.PREFERENCE_BALANCE, String.valueOf(totalBalance));
                        PreferenceManager.putString(activity, Constant.PREFERENCE_BEAN, String.valueOf(totalPoint));
                    }

                    getLocalProfile();
                }
                if (progress.isShowing())
                    progress.dismiss();
            }

            @Override
            public void onFailed() {
                if (progress.isShowing())
                    progress.dismiss();
                Toast.makeText(activity, "Failed to fetching data.", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    @OnClick(R.id.layout_name)
    public void onNameClick() {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra("content", FormActivity.CHANGE_NAME);
        startActivity(intent);
    }

    @OnClick(R.id.layout_email)
    public void onEmailClick() {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra("content", FormActivity.CHANGE_EMAIL);
        startActivity(intent);
    }

    @OnClick(R.id.layout_change_password)
    public void onChangePasswordClick() {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra("content", FormActivity.CHANGE_PASSWORD);
        startActivity(intent);
    }
}

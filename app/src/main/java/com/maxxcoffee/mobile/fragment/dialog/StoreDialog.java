package com.maxxcoffee.mobile.fragment.dialog;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxxcoffee.mobile.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 3/13/2016.
 */
public class StoreDialog extends DialogFragment {

    private String restroomCode = "restroom.png";
    private String seatCode = "seats.png";
    private String wifiCode = "wifi.png";
    private String mallCode = "mall.png";

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.contact)
    TextView contact;
    @Bind(R.id.open)
    TextView open;
    @Bind(R.id.image)
    ImageView image;
    //    @Bind(R.id.slider)
//    SliderLayout slider;
    @Bind(R.id.restroom)
    ImageView restroom;
    @Bind(R.id.seats)
    ImageView seat;
    @Bind(R.id.wifi)
    ImageView wifi;
    @Bind(R.id.mall)
    ImageView mall;

    private Map<String, String> featureMap;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_store);
        dialog.show();

        ButterKnife.bind(this, dialog);

        String mTitle = getArguments().getString("title", "");
        String mAddress = getArguments().getString("address", "");
        String mContact = getArguments().getString("contact", "");
        String mOpen = getArguments().getString("open", "");
        String mClose = getArguments().getString("close", "");
        String mImage = getArguments().getString("image", "");
        String mFeature = getArguments().getString("feature", "");
        String mFeatureIcon = getArguments().getString("feature-icon", "");


        title.setText(mTitle);
        address.setText(mAddress);
        contact.setText(mContact);
        open.setText(mOpen + " - " + mClose);

        Glide.with(getActivity()).load(mImage).placeholder(R.drawable.ic_no_image).crossFade().into(image);

        if (!mFeature.equalsIgnoreCase("") || !mFeatureIcon.equalsIgnoreCase("")) {
            setFeatureMap(mFeature, mFeatureIcon);
        }
//        restroom.setVisibility(mFeature.contains(restroomCode) ? View.VISIBLE : View.INVISIBLE);
//        seat.setVisibility(mFeature.contains(seatCode) ? View.VISIBLE : View.INVISIBLE);
//        wifi.setVisibility(mFeature.contains(wifiCode) ? View.VISIBLE : View.INVISIBLE);
//        mall.setVisibility(mFeature.contains(mallCode) ? View.VISIBLE : View.INVISIBLE);
//
//        restroom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Has restroom.", Toast.LENGTH_LONG).show();
//            }
//        });
//        seat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Has seats.", Toast.LENGTH_LONG).show();
//            }
//        });
//        wifi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Has Wifi", Toast.LENGTH_LONG).show();
//            }
//        });
//        mall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Store location is inside a Mall", Toast.LENGTH_LONG).show();
//            }
//        });

        return dialog;
    }

    private void getFeature() {
        for (final String key : featureMap.keySet()) {
            if (key.contains(restroomCode)) {
                restroom.setVisibility(View.VISIBLE);

                restroom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), featureMap.get(key), Toast.LENGTH_LONG).show();
                    }
                });
            }
            if (key.contains(seatCode)) {
                seat.setVisibility(View.VISIBLE);
                seat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), featureMap.get(key), Toast.LENGTH_LONG).show();
                    }
                });
            }
            if (key.contains(wifiCode)) {
                wifi.setVisibility(View.VISIBLE);
                wifi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), featureMap.get(key), Toast.LENGTH_LONG).show();
                    }
                });
            }
            if (key.contains(mallCode)) {
                mall.setVisibility(View.VISIBLE);
                mall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), featureMap.get(key), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private void setFeatureMap(String mFeature, String mFeatureIcon) {
        featureMap = new HashMap<>();
        List<String> featureList = new Gson().fromJson(mFeature, new TypeToken<List<String>>() {
        }.getType());
        List<String> featureIconList = new Gson().fromJson(mFeatureIcon, new TypeToken<List<String>>() {
        }.getType());

        for (int position = 0; position < featureList.size(); position++) {
            String feature = featureList.get(position);
            String featureIcon = featureIconList.get(position);

            featureMap.put(featureIcon, feature);
        }
        getFeature();
    }
}

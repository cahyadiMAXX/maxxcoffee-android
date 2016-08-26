package com.maxxcoffee.mobile.fragment.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.adapter.StoreImageAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    @Bind(R.id.jarak)
    TextView jarak;
    @Bind(R.id.layout_jarak)
    LinearLayout layoutJarak;
//    @Bind(R.id.image)
//    ImageView image;
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
    @Bind(R.id.layout_location)
    LinearLayout layoutLocation;
    @Bind(R.id.layout_phone)
    LinearLayout layoutPhone;
    @Bind(R.id.slider)
    SliderLayout slider;

    private Map<String, String> featureMap;
    private StoreImageAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_store);
        dialog.show();

        ButterKnife.bind(this, dialog);

        final String mTitle = getArguments().getString("title", "");
        String mAddress = getArguments().getString("address", "");
        final String mContact = getArguments().getString("contact", "");
        String mOpen = getArguments().getString("open", "");
        String mClose = getArguments().getString("close", "");
        String mImage = getArguments().getString("images", "");
        String mFeature = getArguments().getString("feature", "");
        String mFeatureIcon = getArguments().getString("feature-icon", "");
        String mJarak = getArguments().getString("jarak", "");
        final String mLatitude = getArguments().getString("lat", "");
        final String mLongitude = getArguments().getString("lng", "");

        String tempOpen = "", tempClose = "";
        if (mOpen != null) {
            String[] arrayOpen = mOpen.split(":");
            tempOpen = arrayOpen[0] + ":" + arrayOpen[1];
        }
        if (mClose != null) {
            String[] arrayClose = mClose.split(":");
            tempClose = arrayClose[0] + ":" + arrayClose[1];
        }

        title.setText(mTitle);
        address.setText(mAddress);
        contact.setText(mContact);
        open.setText(tempOpen + " - " + tempClose);
        jarak.setText(mJarak);

        layoutJarak.setVisibility(mJarak.equals("-999") ? View.GONE : View.VISIBLE);
//        Glide.with(getActivity()).load(mImage).placeholder(R.drawable.ic_no_image).crossFade().into(image);

        setImages(mImage);

        if (!mFeature.equalsIgnoreCase("") || !mFeatureIcon.equalsIgnoreCase("")) {
            setFeatureMap(mFeature, mFeatureIcon);
        }

        layoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String uri = String.format(Locale.ENGLISH, "geo:%s,%s", mLatitude, mLongitude);
                String uri = "geo:" + mLatitude + "," + mLongitude + "?q=" + mLatitude + "," + mLongitude + "(" + mTitle + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getActivity().startActivity(intent);
            }
        });

        layoutPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mContact.equals("") && !mContact.equals("TBA") && !mContact.equals("N/A")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mContact));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Phone number not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return dialog;
    }

    private void setImages(String mImage) {
        List<String> images = new Gson().fromJson(mImage, new TypeToken<List<String>>() {
        }.getType());
        for (int i = 0; i < images.size(); i++) {
            StoreImageAdapter adapter = new StoreImageAdapter(getContext(), images.get(i));
            slider.addSlider(adapter);
        }
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

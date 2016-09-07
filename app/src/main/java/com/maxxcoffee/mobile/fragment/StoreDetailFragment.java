package com.maxxcoffee.mobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.ui.IconGenerator;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.adapter.StoreImageAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

/**
 * Created by Rio Swarawan on 3/13/2016.
 */
public class StoreDetailFragment extends Fragment {

    private String restroomCode = "restroom.png";
    private String seatCode = "seats.png";
    private String wifiCode = "wifi.png";
    private String mallCode = "mall.png";

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
    @Bind(R.id.no_img)
    ImageView noImg;
    @Bind(R.id.ghost_view_workaround)
    FrameLayout ghost;
    @Bind(R.id.layout_map)
    LinearLayout mapLayout;

    private FormActivity activity;
    private Map<String, String> featureMap;
    private StoreImageAdapter adapter;

    private GoogleMap googleMap;
    private IconGenerator iconFactory;
    private LocationManager locationManager;
    private Criteria criteria;
    private LatLng selectedPosition;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_store, container, false);

        ButterKnife.bind(this, view);

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


        activity.setTitle(mTitle);

        address.setText(mAddress);
        contact.setText(mContact);
        open.setText(tempOpen + " - " + tempClose);
        jarak.setText(mJarak);

        setImages(mImage);
        setMap(mLatitude, mLongitude, mTitle);

        layoutJarak.setVisibility(mJarak.equals("-999") ? View.GONE : View.VISIBLE);

        if (!mFeature.equalsIgnoreCase("") || !mFeatureIcon.equalsIgnoreCase("")) {
            setFeatureMap(mFeature, mFeatureIcon);
        }

//        layoutLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String uri = "geo:" + mLatitude + "," + mLongitude + "?q=" + mLatitude + "," + mLongitude + "(" + mTitle + ")";
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                getActivity().startActivity(intent);
//            }
//        });

        mapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        return view;
    }

    private void setMap(String mLatitude, String mLongitude, final String mTitle) {
        final double lat = Double.parseDouble(mLatitude);
        final double lng = Double.parseDouble(mLongitude);

        iconFactory = new IconGenerator(activity);
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressWarnings("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                StoreDetailFragment.this.googleMap = googleMap;
                StoreDetailFragment.this.googleMap.setMapType(MAP_TYPE_NORMAL);
                StoreDetailFragment.this.googleMap.setMyLocationEnabled(false);

                selectedPosition = new LatLng(lat, lng);
                iconFactory.setStyle(IconGenerator.STYLE_BLUE);
                StoreDetailFragment.this.googleMap.addMarker(new MarkerOptions()
                        .position(selectedPosition)
                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mTitle))));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedPosition, 13));
            }
        });
    }

    private void setImages(String mImage) {
        List<String> images = new Gson().fromJson(mImage, new TypeToken<List<String>>() {
        }.getType());
        if (images.size() == 0) {
            noImg.setVisibility(View.VISIBLE);
        } else if (images.size() == 1) {
            slider.stopAutoCycle();
            ghost.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < images.size(); i++) {
            StoreImageAdapter adapter = new StoreImageAdapter(activity, images.get(i));
            slider.addSlider(adapter);
        }
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = new Dialog(getActivity());
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.detail_store);
//        dialog.show();
//
//        ButterKnife.bind(this, dialog);
//
//        final String mTitle = getArguments().getString("title", "");
//        String mAddress = getArguments().getString("address", "");
//        final String mContact = getArguments().getString("contact", "");
//        String mOpen = getArguments().getString("open", "");
//        String mClose = getArguments().getString("close", "");
//        String mImage = getArguments().getString("image", "");
//        String mFeature = getArguments().getString("feature", "");
//        String mFeatureIcon = getArguments().getString("feature-icon", "");
//        String mJarak = getArguments().getString("jarak", "");
//        final String mLatitude = getArguments().getString("lat", "");
//        final String mLongitude = getArguments().getString("lng", "");
//
//        String tempOpen = "", tempClose = "";
//        if (mOpen != null) {
//            String[] arrayOpen = mOpen.split(":");
//            tempOpen = arrayOpen[0] + ":" + arrayOpen[1];
//        }
//        if (mClose != null) {
//            String[] arrayClose = mClose.split(":");
//            tempClose = arrayClose[0] + ":" + arrayClose[1];
//        }
//
//        title.setText(mTitle);
//        address.setText(mAddress);
//        contact.setText(mContact);
//        open.setText(tempOpen + " - " + tempClose);
//        jarak.setText(mJarak);
//
//        layoutJarak.setVisibility(mJarak.equals("-999") ? View.GONE : View.VISIBLE);
//        Glide.with(getActivity()).load(mImage).placeholder(R.drawable.ic_no_image).crossFade().into(image);
//
//        if (!mFeature.equalsIgnoreCase("") || !mFeatureIcon.equalsIgnoreCase("")) {
//            setFeatureMap(mFeature, mFeatureIcon);
//        }
//
//        layoutLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                String uri = String.format(Locale.ENGLISH, "geo:%s,%s", mLatitude, mLongitude);
//                String uri = "geo:" + mLatitude + "," + mLongitude + "?q=" + mLatitude + "," + mLongitude + "(" + mTitle + ")";
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                getActivity().startActivity(intent);
//            }
//        });
//
//        layoutPhone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!mContact.equals("") && !mContact.equals("TBA") && !mContact.equals("N/A")) {
//                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mContact));
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(getActivity(), "Phone number not valid", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        return dialog;
//    }

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

package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.database.controller.EventController;
import com.maxxcoffee.mobile.database.entity.EventEntity;
import com.maxxcoffee.mobile.util.Constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class DetailEventFragment extends Fragment {

    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.location)
    TextView location;
    @Bind(R.id.location_detail)
    TextView locationDetail;
    @Bind(R.id.layout_detail)
    LinearLayout layoutDetail;
    @Bind(R.id.detail)
    TextView detail;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.phone_layout)
    LinearLayout phoneLayout;

    private FormActivity activity;
    private EventController eventController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
        eventController = new EventController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_event, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Event Detail");

        fetchingData();
        return view;
    }

    private void fetchingData() {
        Integer eventId = getArguments().getInt("event-id");

        EventEntity entity = eventController.getEvent(String.valueOf(eventId));
        if (entity != null) {
            DateFormat mDateFormat = new SimpleDateFormat(Constant.DATEFORMAT_STRING_2);
            Date mStartDate = null;
            Date mEndDate = null;
            StringBuilder mDate = new StringBuilder();
            StringBuilder mTime = new StringBuilder();

            try {
                if (!entity.getTanggal_start().equals("")) {
                    mStartDate = new SimpleDateFormat(Constant.DATEFORMAT_STRING_SIMPLE).parse(entity.getTanggal_start());
                    mDate.append(mDateFormat.format(mStartDate));

                    if (!entity.getTanggal_end().equals("")) {
                        mEndDate = new SimpleDateFormat(Constant.DATEFORMAT_STRING_SIMPLE).parse(entity.getTanggal_end());
                        mDate.append(" - ");
                        mDate.append(mDateFormat.format(mEndDate));
                    }
                }

                if (!entity.getWaktu_start().equals("")) {
                    String[] parseStartTime = entity.getWaktu_start().split(":");
                    mTime.append(parseStartTime[0] + ":" + parseStartTime[1]);

                    if (!entity.getWaktu_end().equals("")) {
                        String[] parseEndTime = entity.getWaktu_end().split(":");
                        mTime.append(" - ");
                        mTime.append(parseEndTime[0] + ":" + parseEndTime[1]);
                    }
                }
                phoneLayout.setVisibility(entity.getNo_telp() == null ? View.GONE : View.VISIBLE);

                Glide.with(activity).load(entity.getGambar()).placeholder(activity.getResources().getDrawable(R.drawable.ic_no_image)).centerCrop().crossFade().into(image);
                title.setText(entity.getNama_event());
                date.setText(mDate.toString());
                time.setText(mTime.toString());
                location.setText(entity.getNama_lokasi());
                locationDetail.setText(entity.getAlamat_lokasi());
                phone.setText(entity.getNo_telp());
                detail.setText(Html.fromHtml(Html.fromHtml(entity.getDeskripsi()).toString()));
                layoutDetail.setVisibility(entity.getDeskripsi().equals("") ? View.GONE : View.VISIBLE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}

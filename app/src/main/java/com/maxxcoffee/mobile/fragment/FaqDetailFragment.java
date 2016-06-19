package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.FaqAdapter;
import com.maxxcoffee.mobile.database.controller.FaqController;
import com.maxxcoffee.mobile.database.entity.FaqEntity;
import com.maxxcoffee.mobile.model.FaqModel;
import com.maxxcoffee.mobile.model.PromoModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.widget.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class FaqDetailFragment extends Fragment {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.description)
    TextView description;

    private FormActivity activity;
    private String token;
    private FaqController faqController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
        token = PreferenceManager.getString(activity, Constant.PREFERENCE_TOKEN, "");

        faqController = new FaqController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq_detail, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("FAQ");

        Integer faqId = getArguments().getInt("faq-id");

        fetchingData(faqId);
        return view;
    }

    private void fetchingData(Integer id) {
        FaqEntity faq = faqController.getFaq(id);
        title.setText(faq.getTitle());
        description.setText(faq.getDescription());
    }
}

package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

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
//    private FaqController faqController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
        token = PreferenceManager.getString(activity, Constant.PREFERENCE_TOKEN, "");

//        faqController = new FaqController(activity);
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
//        FaqEntity faq = faqController.getFaq(id);
//        title.setText(faq.getTitle());
//        description.setText(faq.getDescription());
    }
}

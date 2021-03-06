package com.maxxcoffee.mobile.ui.faq;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.ui.activity.FormActivity;
import com.maxxcoffee.mobile.ui.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.FaqAdapter;
import com.maxxcoffee.mobile.database.controller.FaqController;
import com.maxxcoffee.mobile.database.entity.FaqEntity;
import com.maxxcoffee.mobile.model.FaqModel;
import com.maxxcoffee.mobile.model.response.FaqItemResponseModel;
import com.maxxcoffee.mobile.task.extra.FaqTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;
import com.maxxcoffee.mobile.widget.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class FaqFragment extends Fragment {

    @Bind(R.id.faq_list)
    RecyclerView faqList;

    private MainActivity activity;
    private String token;
    private FaqAdapter adapter;
    private List<FaqModel> data;
    private FaqController faqController;
    private CustomLinearLayoutManager layoutManager;

    public FaqFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);
        token = PreferenceManager.getString(activity, Constant.PREFERENCE_TOKEN, "");

        faqController = new FaqController(activity);
        data = new ArrayList<>();
        adapter = new FaqAdapter(activity, data) {
            @Override
            public void onFaqClick(FaqModel model) {
                Bundle bundle = new Bundle();
                bundle.putInt("faq-id", model.getId());

                Intent intent = new Intent(activity, FormActivity.class);
                intent.putExtra("content", FormActivity.FAQ_DETAIL);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("FAQ");

        layoutManager = new CustomLinearLayoutManager(activity);

        faqList.setLayoutManager(layoutManager);
        faqList.setHasFixedSize(true);
        faqList.setAdapter(adapter);

        if(Utils.isConnected(activity)){
            fetchingData();
        }else{
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void fetchingData() {

        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);*/
        final Dialog loading;
        loading = new Dialog(getActivity());
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);
        loading.show();

        FaqTask task = new FaqTask(activity) {
            @Override
            public void onSuccess(List<FaqItemResponseModel> response) {
                    //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
                if(response.size() > 0)
                    faqController.clearTable();

                if (response != null) {
                    for (FaqItemResponseModel faq : response) {
                        FaqEntity entity = new FaqEntity();
                        entity.setId(faq.getId_faq());
                        entity.setTitle(faq.getQuestion());
                        entity.setDescription(faq.getAnswer());

                        faqController.insert(entity);
                    }
                }

                getLocalFaq();
            }

            @Override
            public void onFailed() {
                    //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
            }
        };
        task.execute();
    }

    private void getLocalFaq() {
        data.clear();
        List<FaqEntity> faqs = faqController.getFaqs();
        for (FaqEntity faq : faqs) {
            FaqModel faqModel = new FaqModel();
            faqModel.setId(faq.getId());
            faqModel.setTitle(faq.getTitle());
            faqModel.setDescription(faq.getDescription());

            data.add(faqModel);
        }
        adapter.notifyDataSetChanged();
    }
}

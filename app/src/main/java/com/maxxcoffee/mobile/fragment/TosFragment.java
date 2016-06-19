package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.FaqAdapter;
import com.maxxcoffee.mobile.database.controller.FaqController;
import com.maxxcoffee.mobile.database.entity.FaqEntity;
import com.maxxcoffee.mobile.model.FaqModel;
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
public class TosFragment extends Fragment {

    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.content)
    TextView content;

    private MainActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tos, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Term of Service");

        fetchingData();
        return view;
    }

    private void fetchingData() {
        content.setText("Lorem ipsum dolor sit amet, in harum epicuri eam, id duo verear tractatos voluptatum, no nec magna dolore. Vel inimicus sententiae definiebas in, tale agam discere te vel. Augue hendrerit eu eum. Ne tollit melius his, recusabo instructior eu nec, id usu melius option liberavisse.\n" +
                "\n" +
                "Brute saepe facete nam cu, ex possit disputando has. Inimicus repudiare in est, per an dicit epicuri. Eius aeterno dolores cum ad. Duo in erat liber cetero, sed sale democritum no, ne per atqui nullam percipit. Dicit graeci ei mei.\n" +
                "\n" +
                "Fabellas pertinacia te pro. Appareat eloquentiam ea his, et sit dictas ocurreret aliquando. Eos cu admodum detracto verterem, bonorum omnesque eloquentiam ut est, consul delicata erroribus et vix. Consequat omittantur ei vis, veniam lucilius qualisque eu pro. Eos velit accumsan ut.\n" +
                "\n" +
                "Illum semper commodo at usu. Augue evertitur mei an. Nec natum nusquam ei, legimus veritus iracundia vel an. Detracto atomorum praesent ei mel, persius necessitatibus in eos. In duo inani perfecto, vero facer consetetur quo ad.\n" +
                "\n" +
                "At case viderer pri, mel cu error instructior, eos dico electram no. Per at omnium eleifend. In vis expetenda delicatissimi, mei et choro aliquam reformidans. Te laudem malorum concludaturque nam, id fugit partem eruditi nec, ei has graeci facilis vituperata. In qui intellegam disputationi.");
    }
}

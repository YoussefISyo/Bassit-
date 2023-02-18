package com.optim.bassit.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.optim.bassit.R;
import com.optim.bassit.data.AppData;

public class PlanFragment extends Fragment {


    private String plan;

    public PlanFragment() {
    }

    public static PlanFragment newInstance(String plan) {
        PlanFragment fragment = new PlanFragment();
        fragment.plan = plan;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_plan, container, false);
        TextView t_plan = vw.findViewById(R.id.t_plan);
        t_plan.setText(t_plan.getText().toString().replace("{0}", AppData.TR(plan)));
        return vw;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

package com.optim.bassit.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.AppData;
import com.optim.bassit.models.Dette;
import com.optim.bassit.ui.activities.FinanceActivity;
import com.optim.bassit.ui.adapters.DetteAdapter;
import com.optim.bassit.ui.dialogs.FixDetteDialogFragment;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetteFragment extends Fragment implements DetteAdapter.ItemClickListener, FinanceActivity.FilterListener {


    @BindView(R.id.list)
    RecyclerView list;

    @Inject
    AppApi appApi;
    private DetteAdapter adapter;

    public DetteFragment() {
    }

    public static DetteFragment newInstance() {
        DetteFragment fragment = new DetteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dettes, container, false);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
        ButterKnife.bind(this, view);

        adapter = new DetteAdapter(this, 0);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);


        getDettes();

        swipe.setOnRefreshListener(this::getDettes);
        return view;
    }

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;

    @BindView(R.id.t_total)
    TextView t_total;

    private void getDettes() {
        swipe.setRefreshing(true);
        appApi.getDettes(AppData.getInstance().fullMonthYear()).enqueue(new Callback<List<Dette>>() {
            @Override
            public void onResponse(Call<List<Dette>> call, Response<List<Dette>> response) {
                if (response.isSuccessful()) {
                    adapter.fill(response.body());
                    double sum = 0;
                    for (Dette one :
                            response.body()) {
                        sum += one.getReste();
                    }

                    t_total.setText(new DecimalFormat("#").format(sum) + " " + getActivity().getString(R.string.text_da));
                }
                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Dette>> call, Throwable t) {
                swipe.setRefreshing(false);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(Dette dette) {
        FragmentManager fm = getChildFragmentManager();
        FixDetteDialogFragment editNameDialogFragment = FixDetteDialogFragment.newInstance(dette.getId(), dette.getMontant(), dette.getPaye(), this);
        editNameDialogFragment.show(fm, "fragment_cloturer");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void doRefreshOnMonthChanged() {
        getDettes();
    }
}

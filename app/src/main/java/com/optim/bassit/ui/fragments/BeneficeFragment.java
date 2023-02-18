package com.optim.bassit.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.AppData;
import com.optim.bassit.models.Dette;
import com.optim.bassit.ui.activities.FinanceActivity;
import com.optim.bassit.ui.adapters.DetteAdapter;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BeneficeFragment extends Fragment implements DetteAdapter.ItemClickListener, FinanceActivity.FilterListener  {


    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;

    @BindView(R.id.t_total)
    TextView t_total;

    @BindView(R.id.bottom_card)
    CardView card;

    @Inject
    AppApi appApi;
    private DetteAdapter adapter;

    public BeneficeFragment() {
    }

    public static BeneficeFragment newInstance() {
        BeneficeFragment fragment = new BeneficeFragment();
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

        adapter = new DetteAdapter(this, 1);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);


        card.setCardBackgroundColor(getActivity().getResources().getColor( R.color.colorPrimaryDark));
        getDettes();

        swipe.setOnRefreshListener(this::getDettes);
        return view;
    }



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
                        sum += one.getMontant() - one.getCharge();
                    }


                    t_total.setText(new DecimalFormat("#").format(sum) + " "+getContext().getString(R.string.text_da));
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

    }

    @Override
    public void doRefreshOnMonthChanged() {
        getDettes();
    }
}

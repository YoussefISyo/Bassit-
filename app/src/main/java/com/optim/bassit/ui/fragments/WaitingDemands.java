package com.optim.bassit.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Customer;
import com.optim.bassit.models.DemandService;
import com.optim.bassit.models.Tache;
import com.optim.bassit.ui.activities.DemandDetailsActivity;
import com.optim.bassit.ui.adapters.DemandsServiceAdapter;
import com.optim.bassit.ui.adapters.TachesAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingDemands extends Fragment implements DemandsServiceAdapter.ItemClickListener{

    private View view;
    @Inject
    AppApi appApi;

    @BindView(R.id.recycler_waiting_demands)
    RecyclerView recycler_waiting_demands;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    DemandsServiceAdapter demandsServiceAdapter;
    List<DemandService> demandServiceList;


    public WaitingDemands(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_waiting_demands, container, false);
        ButterKnife.bind(this, view);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);

        demandServiceList = new ArrayList<>();
        demandsServiceAdapter = new DemandsServiceAdapter(this, getActivity(), appApi);
        recycler_waiting_demands.setAdapter(demandsServiceAdapter);

        refreshApi();

        swipeRefreshLayout.setOnRefreshListener(() -> refreshApi());

        return view;
    }

    private void refreshApi() {
        swipeRefreshLayout.setRefreshing(true);
        DemandService demandService = new DemandService();
        demandService.setState(0);
        if (CurrentUser.getInstance().isPro()){
            demandService.setPro_id(CurrentUser.getInstance().getmCustomer().getId());
            appApi.getDemandsServicePro(0, CurrentUser.getInstance().getmCustomer().getId()).enqueue(new Callback<List<DemandService>>() {
                @Override
                public void onResponse(Call<List<DemandService>> call, Response<List<DemandService>> response) {
                    if (response.isSuccessful()) {
                        demandsServiceAdapter.fill(response.body());
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<List<DemandService>> call, Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }else{
            demandService.setUser_id(CurrentUser.getInstance().getmCustomer().getId());
            appApi.getDemandsServiceUser(0, CurrentUser.getInstance().getmCustomer().getId()).enqueue(new Callback<List<DemandService>>() {
                @Override
                public void onResponse(Call<List<DemandService>> call, Response<List<DemandService>> response) {
                    if (response.isSuccessful()) {
                        demandsServiceAdapter.fill(response.body());
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<List<DemandService>> call, Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }



    }

    public static WaitingDemands newInstance(String text) {

        WaitingDemands f = new WaitingDemands();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }


    @Override
    public void onItemClick(DemandService demandService) {
        Intent mIntent= new Intent(getActivity(), DemandDetailsActivity.class);
        mIntent.putExtra("demand", demandService);
        startActivity(mIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshApi();
    }
}

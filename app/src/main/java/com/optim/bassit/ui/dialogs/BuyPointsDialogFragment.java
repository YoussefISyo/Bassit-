package com.optim.bassit.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Reward;
import com.optim.bassit.ui.adapters.RewardAdapter;
import com.optim.bassit.utils.OptimTools;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyPointsDialogFragment extends DialogFragment implements RewardAdapter.ItemClickListener {

    private static RefreshListener listener;
    @Inject
    AppApi appApi;


    Dialog mDialog;

    public BuyPointsDialogFragment() {
        // Required empty public constructor
    }

    public static BuyPointsDialogFragment newInstance(RefreshListener listener) {
        BuyPointsDialogFragment.listener = listener;
        BuyPointsDialogFragment fragment = new BuyPointsDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
    }

    RewardAdapter adapter;
    @BindView(R.id.loading)
    SpinKitView loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.dialog_fragment_buy_points, container, false);
        ButterKnife.bind(this, vw);
        mDialog = new Dialog(this.getContext());
        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        RecyclerView lst = vw.findViewById(R.id.lst);
        lst.setLayoutManager(new LinearLayoutManager(vw.getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new RewardAdapter(this);
        lst.setAdapter(adapter);

        appApi.getRewards().enqueue(new Callback<List<Reward>>() {
            @Override
            public void onResponse(Call<List<Reward>> call, Response<List<Reward>> response) {
                if (response.isSuccessful()) {
                    adapter.fillMontant(response.body());
                }
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Reward>> call, Throwable t) {
                loading.setVisibility(View.GONE);
            }
        });

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


    @Override
    public void onItemClick(Reward reward) {

    }

    @Override
    public void doBuy(Reward reward) {
        appApi.doBuy(reward.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful())
                    if (response.body().isError())
                        OptimTools.Alert(getContext(), response.body().getMessage());
                    else {
                        {
                            listener.doRefresh();
                            dismiss();
                        }
                    }
                else
                    OptimTools.Alert(getContext(), response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                OptimTools.Alert(getContext(), t.getMessage());
            }
        });
    }

    @Override
    public void doPlan(Reward reward) {
        listener.doRefresh();
    }

    //****************** INTERFACE ***********************
    public interface RefreshListener {
        void doRefresh();
    }
}

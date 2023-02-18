package com.optim.bassit.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseFragment;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.AppData;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Journal;
import com.optim.bassit.ui.activities.FinanceActivity;
import com.optim.bassit.ui.adapters.ConsommationAdapter;
import com.optim.bassit.ui.dialogs.MontantDialogFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TresorFragment extends BaseFragment implements ConsommationAdapter.ItemClickListener, FinanceActivity.FilterListener  {


    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;

    @BindView(R.id.t_total)
    TextView t_total;

    @BindView(R.id.bottom_card)
    CardView card;
    @BindView(R.id.b_add_montant)
    FloatingActionButton b_add;

    @Inject
    AppApi appApi;
    private ConsommationAdapter adapter;

    public TresorFragment() {
    }

    public static TresorFragment newInstance() {
        TresorFragment fragment = new TresorFragment();
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

        adapter = new ConsommationAdapter(this, 10);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);

        card.setVisibility(View.GONE);
        b_add.show();
        getTresor();

        swipe.setOnRefreshListener(this::getTresor);
        return view;
    }


    public void getTresor() {
        swipe.setRefreshing(true);
        appApi.getTresor(AppData.getInstance().fullMonthYear()).enqueue(new Callback<List<Journal>>() {
            @Override
            public void onResponse(Call<List<Journal>> call, Response<List<Journal>> response) {
                if (response.isSuccessful()) {
                    adapter.fill(response.body());
                }
                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Journal>> call, Throwable t) {
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
    public void onItemClick(Journal journal) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.app_name)
                .setMessage("Modifier ou supprimer ?")
                .setPositiveButton(R.string.text_edit, (dialogInterface, i) -> {
                    FragmentManager fm = getChildFragmentManager();
                    MontantDialogFragment editNameDialogFragment = MontantDialogFragment.newInstance(this, journal);
                    editNameDialogFragment.show(fm, "fragment_cloturer");
                    dialogInterface.dismiss();
                })
                .setNegativeButton(R.string.text_delete, (dialogInterface, i) -> {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.app_name)
                            .setMessage("veuillez confirmer la suppression de l'élément sélectionné [" + journal.getDesignation() + "]?")
                            .setPositiveButton(R.string.text_confirm, (d, ii) -> {
                                appApi.deleteTresor(journal.getId()).enqueue(new Callback<ApiResponse>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                        if (response.isSuccessful() && !response.body().isError())
                                        {
                                            Alert("Suppression terminé avec succès");
                                            ((FinanceActivity)getActivity()).setBreload();
                                        }
                                        else if (response.isSuccessful())
                                            Alert(response.body().getMessage());
                                        else
                                            Alert(response.errorBody().toString());

                                    }

                                    @Override
                                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                                        Alert(t.getMessage());
                                    }
                                });
                                d.dismiss();
                            })
                            .setNegativeButton(R.string.text_cancel, null)
                            .show();
                    dialogInterface.dismiss();
                })
                .show();
    }


    @Override
    public boolean onLongItemClick(Journal journal) {
        return false;
    }

    @OnClick(R.id.b_add_montant)
    public void onClick() {
        FragmentManager fm = getChildFragmentManager();
        MontantDialogFragment editNameDialogFragment = MontantDialogFragment.newInstance(this);
        editNameDialogFragment.show(fm, "fragment_cloturer");
    }

    @Override
    public void doRefreshOnMonthChanged() {
        getTresor();
    }
}

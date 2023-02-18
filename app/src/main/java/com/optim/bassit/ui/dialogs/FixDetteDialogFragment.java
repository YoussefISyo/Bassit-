package com.optim.bassit.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseDialog;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.ui.activities.FinanceActivity;
import com.optim.bassit.utils.OptimTools;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FixDetteDialogFragment extends BaseDialog {

    @Inject
    AppApi appApi;

    @BindView(R.id.t_montant)
    EditText t_montant;
    @BindView(R.id.t_payed)
    EditText t_payed;
    @BindView(R.id.t_reste)
    TextView t_reste;


    Dialog mDialog;
    private int bon_id;
    private String omontant;
    private String opaye;
    private FinanceActivity.FilterListener listener;

    public FixDetteDialogFragment() {

    }

    public static FixDetteDialogFragment newInstance(int bon_id, double m, double p, FinanceActivity.FilterListener listener) {
        FixDetteDialogFragment fragment = new FixDetteDialogFragment();
        fragment.bon_id = bon_id;
        fragment.listener = listener;
        fragment.omontant = new DecimalFormat("#").format(m);
        fragment.opaye = new DecimalFormat("#").format(p);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fixdette_dialog_fragment, container, false);
        ButterKnife.bind(this, vw);
        mDialog = new Dialog(this.getContext());

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        t_montant.addTextChangedListener(getWatcher());
        t_payed.addTextChangedListener(getWatcher());

        t_montant.setText(omontant);
        t_payed.setText(opaye);
        onChanged();
        return vw;
    }

    @NotNull
    private TextWatcher getWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private void onChanged() {
        double montant = OptimTools.toDouble(t_montant);
        double payed = OptimTools.toDouble(t_payed);
        t_reste.setText(getContext().getString(R.string.reste_egal) + new DecimalFormat("#").format(montant - payed));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @OnClick(R.id.b_cancel)
    public void doCancel() {
        dismiss();
    }

    @OnClick(R.id.b_confirmer)
    public void doConfirmer() {
        double montant = OptimTools.toDouble(t_montant);
        double payed = OptimTools.toDouble(t_payed);
        if (payed > montant) {
            Alert(R.string.pay_bigger_amount);
            return;
        }
        appApi.doFixDette(bon_id, OptimTools.toDouble(t_montant), OptimTools.toDouble(t_payed))
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle(R.string.app_name)
                                        .setMessage(R.string.dette_mis_a_jour)
                                        .setPositiveButton(R.string.text_ok, (dialogInterface, i) -> {

                                            listener.doRefreshOnMonthChanged();
                                            dismiss();

                                        }).show();

                            } else {
                                Alert(response.body().getMessage());
                            }
                        } else
                            Alert(response.errorBody().toString());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Alert(t.getMessage());
                    }
                });
    }
}
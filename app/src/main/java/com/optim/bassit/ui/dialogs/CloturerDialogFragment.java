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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseDialog;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.offerchat;
import com.optim.bassit.utils.OptimTools;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CloturerDialogFragment extends BaseDialog {

    @Inject
    AppApi appApi;

    @BindView(R.id.t_montant)
    EditText t_montant;
    @BindView(R.id.t_charge)
    EditText t_charge;
    @BindView(R.id.t_payed)
    EditText t_payed;
    @BindView(R.id.t_reste)
    TextView t_reste;
    @BindView(R.id.textView18)
    TextView textView18;
    @BindView(R.id.spinner2)
    Spinner spinner;
    @BindView(R.id.r_avis)
    RatingBar r_avis;
    @BindView(R.id.t_comment)
    EditText t_comment;

    Dialog mDialog;
    private int bon_id;
    String offer_id="";
    String typec="";
    String from="";
    public CloturerDialogFragment() {

    }

    public static CloturerDialogFragment newInstance(int bon_id,String typec,String offer_id,String from) {
        CloturerDialogFragment fragment = new CloturerDialogFragment();
        fragment.bon_id = bon_id;
        fragment.offer_id = offer_id;
        fragment.typec = typec;
        fragment.from = from;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.cloturer_dialog_fragment, container, false);
        ButterKnife.bind(this, vw);
        mDialog = new Dialog(this.getContext());

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        t_montant.addTextChangedListener(getWatcher());
        t_charge.addTextChangedListener(getWatcher());
        t_payed.addTextChangedListener(getWatcher());
        if(from.equals("myoffer")) {

            appApi.getofferchat(offer_id).enqueue(new Callback<List<offerchat>>() {
                @Override
                public void onResponse(Call<List<offerchat>> call, Response<List<offerchat>> response) {

                    //   Toast.makeText(getContext(),"su",Toast.LENGTH_LONG).show();
                    if (response.isSuccessful()) {


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        List<String> offers = new ArrayList<>();

                        for (offerchat offer : response.body()) {
                          //  if(!offers_id.contains(offer.getId())){
                           offers.add(offer.getNom());
                            offers_id.add(offer.getId());//}
                        }
                       // offers.add("");
                      //  offers_id.add(0);
                        adapter.clear();
                        adapter.addAll(offers);
                        adapter.notifyDataSetChanged();
                        spinner.setVisibility(View.VISIBLE);
                        textView18.setVisibility(View.VISIBLE);
                    } else {
                        new AlertDialog.Builder(getContext())
                                .setTitle(R.string.app_name)
                                .setMessage(R.string.tryagain)
                                .setPositiveButton(R.string.text_ok, (dialogInterface, i) -> {
                                    dismiss();

                                }).show();
                    }

                }

                @Override
                public void onFailure(Call<List<offerchat>> call, Throwable t) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.app_name)
                            .setMessage(R.string.tryagain)
                            .setPositiveButton(R.string.text_ok, (dialogInterface, i) -> {
                                dismiss();

                            }).show();
                }
            });
        }
        return vw;
    }
    List<Integer> offers_id=new ArrayList<Integer>();
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
        t_reste.setText("Reste = " + new DecimalFormat("#").format(montant - payed));
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
        if(from.equals("myoffer"))
            bon_id=offers_id.get(spinner.getSelectedItemPosition());
        appApi.doCloturer(bon_id,typec,offer_id, OptimTools.toDouble(t_montant), OptimTools.toDouble(t_charge), OptimTools.toDouble(t_payed), r_avis.getRating(), t_comment.getText().toString())
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle(R.string.app_name)
                                        .setMessage(R.string.text_tache_cloture)
                                        .setPositiveButton(R.string.text_ok, (dialogInterface, i) -> {
                                            getActivity().setResult(10077);
                                            dismiss();
                                            getActivity().finish();

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
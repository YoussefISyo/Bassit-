package com.optim.bassit.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

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
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.Tache;
import com.optim.bassit.models.offerchat;
import com.optim.bassit.utils.OptimTools;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.optim.bassit.utils.OptimTools.Alert;

public class EvaluationDialogFragment extends DialogFragment {

    @Inject
    AppApi appApi;

    @BindView(R.id.t_comment)
    EditText t_comment;

    @BindView(R.id.rating_bar)
    RatingBar r_avis;
    @BindView(R.id.textView18)
    TextView textView18;
    @BindView(R.id.spinner2)
    Spinner spinner;
    private int bon_id;
    String offer_id="";
    String typec="";
    String from="";
    public EvaluationDialogFragment() {
        // Required empty public constructor
    }
    public static EvaluationDialogFragment newInstance(int bon_id,String typec,String offer_id,String from) {
        EvaluationDialogFragment fragment = new EvaluationDialogFragment();
        fragment.bon_id = bon_id;
        fragment.offer_id = offer_id;
        fragment.typec = typec;
        fragment.from = from;
        return fragment;
    }
    Tache mTache;

    public static EvaluationDialogFragment newInstance(int bon_id) {
        EvaluationDialogFragment fragment = new EvaluationDialogFragment();
        fragment.bon_id = bon_id;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
        if(!from.equals("myoffer"))
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.dialog_fragment_evaluation, container, false);
        ButterKnife.bind(this, vw);

        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        if(from.equals("myoffer")) {
           // getDialog().setCancelable(true);
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
                        //if(bon_id!=0)getselectedbon();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @OnClick(R.id.btn_ok)
    public void okClick() {
        if(from.equals("myoffer") ){
            if(offers_id.size()>0)
                bon_id=offers_id.get(spinner.getSelectedItemPosition());
            else { new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.app_name)
                    .setMessage(getContext().getString(R.string.no_pro_fini))
                    .setPositiveButton(R.string.text_ok, (dialogInterface, i) -> {
                        dismiss();

                    }).show();
            return;
            }

        }

        appApi.doRate(bon_id,typec,offer_id, r_avis.getRating(), t_comment.getText().toString())
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle(R.string.app_name)
                                        .setMessage(getContext().getString(R.string.tach_fini))
                                        .setPositiveButton(R.string.text_ok, (dialogInterface, i) -> {
                                            dismiss();

                                        }).show();

                            } else {
                                OptimTools.Alert(getActivity(), response.body().getMessage());
                            }
                        } else
                            OptimTools.Alert(getActivity(), response.errorBody().toString());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        OptimTools.Alert(getActivity(), t.getMessage());
                    }
                });
    }

}

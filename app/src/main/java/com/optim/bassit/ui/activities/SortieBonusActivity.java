package com.optim.bassit.ui.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SortieBonusActivity extends BaseActivity {

    @BindView(R.id.b_back)
    ImageButton Back_btn;

    @BindView(R.id.t_montant)
    EditText t_montant;
    @BindView(R.id.t_reference_amis)
    EditText t_reference_amis;

    @BindView(R.id.t_title)
    TextView t_title;
    @BindView(R.id.mon_ccp)
    TextView mon_ccp;

    @BindView(R.id.t_note)
    EditText t_note;
    @BindView(R.id.ch_pro)
    Switch ch_pro;
    @BindView(R.id.sp_amis2)
    LinearLayout sp_amis2;
    @Inject
    AppApi appApi;
    private boolean isclient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortie_bonus);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        setStatusBarColor(R.color.black, false);
        Back_btn.setOnClickListener(v -> finish());

        isclient = getIntent().getBooleanExtra("isclient", true);
        if (isclient) {
            t_title.setText(R.string.text_versement_ccp);
            findViewById(R.id.sp_amis).setVisibility(View.GONE);
            findViewById(R.id.sp_amis2).setVisibility(View.GONE);

            mon_ccp.setText(CurrentUser.getInstance().getmCustomer().getNomprenomccp() + "\n" + CurrentUser.getInstance().getmCustomer().getCcp());

        } else {

            t_title.setText(R.string.transfert_bassit);
            mon_ccp.setVisibility(View.GONE);
        }

        ((TextView) findViewById(R.id.t_total)).setText(CurrentUser.getInstance().getmCustomer().getBonus() + " " + getString(R.string.text_da))
        ;
        ch_pro.setOnCheckedChangeListener((compoundButton, b) -> {
            if (ch_pro.isChecked())
                findViewById(R.id.sp_amis).setVisibility(View.GONE);
            else
                findViewById(R.id.sp_amis).setVisibility(View.VISIBLE);
        });
        if (!CurrentUser.getInstance().isPro()) {
            sp_amis2.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.b_confirmer})
    public void onConfirmerClick() {


        Integer montant = 0;

        try {
            montant = Integer.valueOf(t_montant.getText().toString());

        } catch (Exception ex) {
            Alert(R.string.montant_invalide);
        }

        String note = t_note.getText().toString();

        if (montant == 0) {
            Alert(R.string.montant_invalide);
            return;
        }

        if (montant < 100) {
            Alert(R.string.message_less_100);
            return;
        }

        if (montant > CurrentUser.getInstance().getmCustomer().getBonus()) {
            Alert(R.string.bonus_insuff);
            return;
        }
        Integer refe = 0;

        Integer finalMontant = montant;

        if (!CurrentUser.getInstance().isPro()) {
            if (ch_pro.isChecked()) {
                new AlertDialog.Builder(SortieBonusActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(getString(R.string.etes_vous_sur_de_vouloir_transferer) + " " + montant + " " + getString(R.string.da_vert_votre_compte_pro))
                        .setPositiveButton(R.string.text_confirm, (a, b) -> {
                            show();
                            appApi.doBonusToCredit(note, finalMontant).enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call2, Response<ApiResponse> response2) {
                                    hide();
                                    if (!response2.isSuccessful())
                                        Alert(response2.errorBody().toString());
                                    else if (response2.body().isError()) {
                                        Alert(response2.body().getMessage());
                                    } else {
                                        setResult(10066);
                                        SortieBonusActivity.this.finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call2, Throwable t2) {
                                    hide();
                                    Alert(t2.getMessage());

                                }
                            });
                            a.dismiss();
                        })
                        .setNegativeButton(R.string.text_cancel, null).show();

                return;
            }


        }

        if (!isclient) {


            try {
                refe = Integer.valueOf(t_reference_amis.getText().toString());

            } catch (Exception ex) {
                Alert(R.string.reference_invalide);
            }


            if (refe < 3986) {
                Alert(R.string.reference_invalide);
                return;
            }
        }
        Integer finalRefe = refe;

        appApi.userByRef(refe).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (!response.isSuccessful())
                    Alert(response.errorBody().toString());
                else if (response.body().isError() && !isclient)
                    Alert(response.body().getMessage());
                else {
                    String message = isclient ? getString(R.string.etes_vous_sur_de_vouloir_transferer) + " " + finalMontant + " " + getString(R.string.da_vers_ccp) :
                            getString(R.string.etes_vous_sur_de_vouloir_transferer) + " " + finalMontant + getString(R.string.text_da) + " " + getString(R.string.text_a) + " " + response.body().getMessage();
                    new AlertDialog.Builder(SortieBonusActivity.this)
                            .setTitle(R.string.app_name)
                            .setMessage(message)
                            .setPositiveButton(R.string.text_confirm, (a, b) -> {
                                show();
                                appApi.doSortie(note, finalMontant, finalRefe).enqueue(new Callback<ApiResponse>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse> call2, Response<ApiResponse> response2) {
                                        hide();
                                        if (!response2.isSuccessful())
                                            Alert(response2.errorBody().toString());
                                        else if (response2.body().isError()) {
                                            Alert(response2.body().getMessage());
                                        } else {
                                            setResult(10066);
                                            SortieBonusActivity.this.finish();
                                        }
                                    }


                                    @Override
                                    public void onFailure(Call<ApiResponse> call2, Throwable t2) {
                                        hide();
                                        Alert(t2.getMessage());

                                    }
                                });
                                a.dismiss();
                            })
                            .setNegativeButton(R.string.text_cancel, null).show();

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });


    }


    @OnClick({R.id.b_cancel})
    public void onAnnulerClick() {
        this.finish();
    }


}

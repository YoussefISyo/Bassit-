package com.optim.bassit.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Customer;
import com.optim.bassit.utils.MapHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.optim.bassit.utils.OptimTools.Alert;

public class UpdateMyProfileActivity extends BaseActivity {


    @BindView(R.id.et_description)
    EditText tDescription;
    @BindView(R.id.t_full_address)
    EditText t_full_address;
    @BindView(R.id.t_address)
    TextView t_address;
    @BindView(R.id.t_bname)
    TextView t_bname;

    @BindView(R.id.b_back)
    ImageButton Back_btn;
    @Inject
    AppApi appApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_profile);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);


        if (CurrentUser.getInstance().getmCustomer() != null) {
            cus = CurrentUser.getInstance().getmCustomer();

            tDescription.setText(cus.getDescription());
            t_full_address.setText(cus.getAddress());
            t_bname.setText(cus.getBname());


            if (cus.getBGPSAddress() == null || cus.getBGPSAddress().matches(""))
                t_address.setVisibility(View.GONE);
            else {
                t_address.setVisibility(View.VISIBLE);
                t_address.setText(cus.getBGPSAddress());
            }

            cus = CurrentUser.getInstance().getmCustomer();


            Back_btn.setOnClickListener(v -> finish());

            if (CurrentUser.getInstance().getmCustomer().getPause() == 1) {
                Button b_pause = findViewById(R.id.b_pause);
                b_pause.setText(R.string.activer_mes_services);
                b_pause.setBackground(getResources().getDrawable(R.drawable.round_button));
            }
            Customer mCustomer = CurrentUser.getInstance().getmCustomer();
            if (!(mCustomer.getTherole() == 2 || mCustomer.getTherole() == 33)) {
                findViewById(R.id.l_inputs).setVisibility(View.GONE);
                findViewById(R.id.b_pause).setVisibility(View.GONE);
                findViewById(R.id.l_gps).setVisibility(View.GONE);
                findViewById(R.id.t_address).setVisibility(View.GONE);
            }

        }

    }

    @OnClick({R.id.b_pause})
    public void pauseAccount() {
        AlertYesNo(CurrentUser.getInstance().getmCustomer().getPause() == 1 ? getString(R.string.etes_vous_sur_de_vouloir_activer_tous_vos_service) : getString(R.string.etes_vous_sur_de_vouloir_mettre_tous_vos_service_en_pause),
                CurrentUser.getInstance().getmCustomer().getPause() == 1 ? getString(R.string.text_activer) : getString(R.string.text_pause),
                getString(R.string.text_cancel), () -> {

                    show();

                    int bpause = CurrentUser.getInstance().getmCustomer().getPause();
                    fullyHandleResponseSuccessOnly(appApi.pauseUser(), () -> {
                        CurrentUser.getInstance().getmCustomer().setPause(bpause == 1 ? 0 : 1);
                        setResult(10007);
                        finish();
                    });

                });
    }

    @OnClick({R.id.b_confirmer})
    public void onConfirmerClick() {

        show();
       // isempty(t_full_address)
        if ( isempty(t_bname) || isempty(tDescription)) {
            Alert(R.string.remplir_les_champs);
            hide();
            return;
        }
        if (cus.getBcountry() == null || cus.getBcountry().matches("")) {
            Alert(R.string.veuillez_determiner_votre_position);
            hide();
            return;
        }

        cus.setAddress(t_full_address.getText().toString());
        cus.setDescription(tDescription.getText().toString());
        cus.setBname(t_bname.getText().toString());

        appApi.updateBusiness(cus).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    appApi.customerProfile(0).enqueue(new Callback<Customer>() {
                        @Override
                        public void onResponse(Call<Customer> call, Response<Customer> response) {
                            if (response.isSuccessful()) {
                                Customer customer = response.body();
                                CurrentUser.getInstance().setmCustomer(customer);
                            }
                            setResult(10008);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Customer> call, Throwable t) {
                            finish();
                        }
                    });

                } else {
                    Alert(R.string.error_server);
                    hide();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Alert(R.string.error_server);
                hide();
            }
        });

    }

    @OnClick({R.id.b_cancel})
    public void onAnnulerClick() {
        this.finish();
    }

    @OnClick({R.id.b_edit_info})
    public void onEditInfo() {
        Intent intent_profile = new Intent(this, EditInformationActivity.class);
        startActivityForResult(intent_profile, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (CurrentUser.getInstance().getmCustomer() == null) {


            cus.setBcountry(CurrentUser.getInstance().getmCustomer().getBcountry());
            cus.setBwilaya(CurrentUser.getInstance().getmCustomer().getBwilaya());
            cus.setBcity(CurrentUser.getInstance().getmCustomer().getBcity());
            cus.setLat(CurrentUser.getInstance().getmCustomer().getLat());
            cus.setLon(CurrentUser.getInstance().getmCustomer().getLon());

        }
        if (cus.getBGPSAddress() == null || cus.getBGPSAddress().matches(""))
            t_address.setVisibility(View.GONE);
        else {
            t_address.setVisibility(View.VISIBLE);
            t_address.setText(cus.getBGPSAddress());
        }

    }

    @OnClick({R.id.b_edit_position})
    public void onEditPosition() {

        startActivityForResult(new Intent(this, MapActivity.class), 0);


    }

    Customer cus;
}
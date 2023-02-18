package com.optim.bassit.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Customer;
import com.optim.bassit.utils.LocaleHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditInformationActivity extends BaseActivity {

    public static final int INSCRIPTION_CODE = 9001;
    @BindView(R.id.et_nom)
    EditText tNom;
    @BindView(R.id.et_prenom)
    EditText tPrenom;
    @BindView(R.id.et_email)
    EditText tEmail;
    @BindView(R.id.t_ccp)
    EditText tCCP;
    @BindView(R.id.t_nomprenomccp)
    EditText tNomPrenomCCP;
    @BindView(R.id.et_telephone)
    EditText tPhone;
    @BindView(R.id.et_pwd)
    EditText tPwd;
    @BindView(R.id.et_pwd2)
    EditText tPwd2;
    @BindView(R.id.t_old)
    EditText tOld;
    @BindView(R.id.et_description)
    EditText et_description;
    @BindView(R.id.et_adresse)
    EditText et_adresse;
    @BindView(R.id.switch_pause)
    Switch switch_pause;
    @BindView(R.id.txt_switch)
    TextView txt_switch;
    @BindView(R.id.b_annuler)
    Button b_annuler;
    @Inject
    AppApi appApi;

    Toolbar mToolbarDetailsInfo;
    String lon = "", lat = "", address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_edit_information);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);
        Customer cus = CurrentUser.getInstance().getmCustomer();
        tNom.setText(cus.getName());
        tPrenom.setText(cus.getPrenom());
        tEmail.setText(cus.getEmail());
        tPhone.setText(cus.getPhone());
        tCCP.setText(cus.getCcp());
        tNomPrenomCCP.setText(cus.getNomprenomccp());
        et_description.setText(cus.getDescription());
        et_adresse.setText(cus.getAddress());
        address = cus.getAddress();
        lon = cus.getLon();
        lat = cus.getLat();

        et_adresse.setOnClickListener(view -> getAddress());

        if (CurrentUser.getInstance().getmCustomer().getPause() == 1) {
            txt_switch.setText(R.string.activer_mes_services);
            switch_pause.setChecked(false);
        }else{
            txt_switch.setText(R.string.pause_mes_services);
            switch_pause.setChecked(true);
        }

        switch_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseAccount();
            }
        });

        mToolbarDetailsInfo = findViewById(R.id.mToolbarDetailsInfo);


        setSupportActionBar(mToolbarDetailsInfo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_black);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }

        if (LocaleHelper.isRTL()) {
            tPwd.setGravity(Gravity.RIGHT);
            tPwd2.setGravity(Gravity.RIGHT);
            tOld.setGravity(Gravity.RIGHT);
        }
    }

    private void getAddress() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("user", false);
        intent.putExtra("updateInfo", true);
        intent.putExtra("new", false);
        startActivityForResult(intent, 104);
    }

    @OnClick(R.id.b_inscrire)
    public void InscrireClick() {

        if (tNom.getText().toString().matches("") || tPrenom.getText().toString().matches("")) {
            Alert(R.string.message_fill_required_fields);
            return;
        }


        if (!tOld.getText().toString().matches("") && (tPwd.getText().toString().matches("") || tPwd2.getText().toString().matches(""))) {
            Alert(R.string.message_fill_required_fields);
            return;
        }

        if (tOld.getText().toString().matches("") && (!tPwd.getText().toString().matches("") || !tPwd2.getText().toString().matches(""))) {
            Alert(R.string.fill_old_password);
            return;
        }


        if (!tOld.getText().toString().matches("")) {
            if (!tPwd.getText().toString().equals(tPwd2.getText().toString())) {
                Alert(R.string.message_confirm_password);
                return;
            }
        }

        Customer customer = new Customer();

        customer.setName(tNom.getText().toString());
        customer.setPrenom(tPrenom.getText().toString());
        customer.setPhone(tPhone.getText().toString());
        customer.setCcp(tCCP.getText().toString());
        customer.setNomprenomccp(tNomPrenomCCP.getText().toString());
        customer.fillTempPWD(tOld.getText().toString(), tPwd.getText().toString(), tPwd2.getText().toString());
        customer.setDescription(et_description.getText().toString());
        customer.setAddress(et_adresse.getText().toString());
        customer.setLon(lon);
        customer.setLat(lat);


        show( );
        appApi.updateCustomer(customer).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                hide( );
                if (!response.isSuccessful()) {
                    Alert(response.errorBody().toString());
                } else if (response.body().isError()) {
                    Alert(response.body().getMessage());
                } else {
                    CurrentUser.getInstance().getmCustomer().setName(customer.getName());
                    CurrentUser.getInstance().getmCustomer().setPrenom(customer.getPrenom());
                    CurrentUser.getInstance().getmCustomer().setPhone(customer.getPhone());
                    CurrentUser.getInstance().getmCustomer().setCcp(customer.getCcp());
                    CurrentUser.getInstance().getmCustomer().setNomprenomccp(customer.getNomprenomccp());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hide( );
                Alert(t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


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

    @OnClick(R.id.b_annuler)
    public void annuler(){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 104) {
            address = data.getStringExtra("address");
            lon = data.getStringExtra("longitude");
            lat = data.getStringExtra("latitude");

            if (!address.matches("")){
                et_adresse.setText(address);
            }
        }
    }
}

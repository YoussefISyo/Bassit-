package com.optim.bassit.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.models.HomeadsFeed;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class repreqAddEditActivity extends BaseActivity {




    private boolean isNew;

    @BindView(R.id.et_date)
    EditText tdate;
    @BindView(R.id.et_price)
    EditText tprice;
    @BindView(R.id.et_description)
    EditText tDescription;



    String id="0";
    @Inject
    AppApi appApi;
    HomeadsFeed mHomeFeed;
    ArrayList<String> list_cat;
    DatePickerDialog picker;
    String change="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repreq_add_edit);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);







        if (getIntent().getExtras() != null) {
          id= getIntent().getStringExtra("id");
          change= getIntent().getStringExtra("change");
          if(!change.isEmpty()){
              tdate.setText(getIntent().getStringExtra("dur"));
              tprice.setText(getIntent().getStringExtra("price"));
              tDescription.setText(getIntent().getStringExtra("des"));

          }


        }




    }





    @OnClick({R.id.b_confirmer})
    public void onConfirmerClick() {



        String description = tDescription.getText().toString();
        String date = tdate.getText().toString();
        String price = tprice.getText().toString();
        if (description.matches("") || description.matches("") || price.trim().equals("")|| date.trim().equals("")) {
            Alert(R.string.message_fill_required_fields);
            return;
        }

        show();
//        appApi.addEditrepreq(CurrentUser.getInstance().getmCustomer().getId()+"",id,description,date,price,change,"","")
//                .enqueue(new Callback<ApiResponse>() {
//                    @Override
//                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
//                        hide();
//                        if (!response.isSuccessful() || response.body().isError()) {
//                            Alert(response.body().getMessage());
//                        } else {
//                            repreqAddEditActivity.this.finish();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ApiResponse> call, Throwable t) {
//                        hide();
//                        Alert(t.getMessage());
//
//                    }
//                });


    }



    @OnClick({R.id.b_cancel})
    public void onAnnulerClick() {
        this.finish();
    }



   /* @OnClick({R.id.b_pause})
    public void PauseService() {

        AlertYesNo(mHomeFeed.getPause() == 1 ? getString(R.string.service_pause_confirmation) : getString(R.string.service_activate_confirmation), getString(mHomeFeed.getPause() == 1 ?
                        R.string.text_activer : R.string.text_pause),
                getString(R.string.text_cancel), () -> {

                    show();

                    fullyHandleResponseSuccessOnly(appApi.pauseService(mHomeFeed.getId()), () -> {
                        hide();
                        finish();
                    });

                });

    }

    @OnClick({R.id.b_delete})
    public void DeleteService() {

        AlertYesNo(R.string.confirm_delete_service, getString(R.string.text_delete), getString(R.string.text_cancel), () -> {

            show();

            fullyHandleResponseSuccessOnly(appApi.deleteService(mHomeFeed.getId()), () -> {
                hide();
                setResult(10022);
                finish();
            });
        });
    }*/
}

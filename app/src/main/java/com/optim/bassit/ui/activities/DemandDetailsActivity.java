package com.optim.bassit.ui.activities;

import static com.optim.bassit.ui.activities.MainActivity.context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Commission;
import com.optim.bassit.models.DemandService;
import com.optim.bassit.utils.OptimTools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DemandDetailsActivity extends BaseActivity {

    @Inject
    AppApi appApi;

    @BindView(R.id.mToolbarDetailsDemand)
            Toolbar mToolbarDetailsDemand;

    @BindView(R.id.t_username)
            TextView t_username;
    @BindView(R.id.t_city)
            TextView t_city;
    @BindView(R.id.txt_urgence)
            RelativeLayout txt_urgence;
    @BindView(R.id.t_title)
            TextView t_title;
    @BindView(R.id.msg)
            TextView category;
    @BindView(R.id.edt_unity)
            TextView edt_unity;
    @BindView(R.id.edt_quantity)
            TextView edt_quantity;
    @BindView(R.id.tDateStart)
            TextView tDateStart;
    @BindView(R.id.tdatefin)
            TextView tdatefin;
    @BindView(R.id.t_content)
            TextView t_content;
    @BindView(R.id.btn_finished)
            Button btn_finished;
    @BindView(R.id.btn_accept)
            Button btn_accept;
    @BindView(R.id.profile_img)
    CircleImageView profile_img;
    @BindView(R.id.img_active_user)
    ImageView img_active_user;
    @BindView(R.id.txt_active)
            TextView txt_active;
    @BindView(R.id.img_official_account)
            ImageView img_official_account;
    @BindView(R.id.btn_extend)
    Button btn_extend;
    @BindView(R.id.container_phone)
    RelativeLayout container_phone;
    @BindView(R.id.viewContact)
            View viewContact;
    @BindView(R.id.t_phone)
            TextView t_phone;

    DemandService demandService;
    DatePickerDialog picker;
    List<String> unities;
    int negociate = 1;
    String commission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_details);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        demandService = getIntent().getParcelableExtra("demand");

        setSupportActionBar(mToolbarDetailsDemand);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);

        init();
    }

    private void init() {

        t_username.setText(demandService.getUser_name());
        t_title.setText(demandService.getService_title());
        category.setText(demandService.getCategory());
        t_content.setText(demandService.getDescription());
        edt_quantity.setText(String.valueOf(demandService.getQuantity()));
        edt_unity.setText(demandService.getUnity());
        tDateStart.setText(demandService.getDate_start() + " /");
        tdatefin.setText(demandService.getDate_end());
        t_city.setText(demandService.getCity());
        t_phone.setText(demandService.getPhone());

        if (demandService.getImage_user() != null){
            OptimTools.getPicasso(demandService.getClientPinLink()).into(profile_img);
        }

        t_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(t_phone.getText());
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        if ((demandService.getUrgence() == 0)) { txt_urgence.setVisibility(View.GONE); } else { txt_urgence.setVisibility(View.VISIBLE); }

        if (demandService.getState() == 0){
            btn_accept.setVisibility(View.VISIBLE);
            btn_finished.setVisibility(View.GONE);
            container_phone.setVisibility(View.GONE);
            viewContact.setVisibility(View.GONE);
        }else if (demandService.getState() == 1){
            if (CurrentUser.getInstance().isPro()){
                calculCommission();
                btn_finished.setVisibility(View.VISIBLE);
                if (demandService.getTime_extend() == 0){

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date endDate = sdf.parse(demandService.getDate_end());

                        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                        Date todayDat = sdf.parse(date);

                        if ((printDifference(todayDat, endDate) < 8) && (printDifference(todayDat, endDate) > 0)){
                            btn_extend.setVisibility(View.VISIBLE);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                btn_finished.setVisibility(View.GONE);
            }
            btn_accept.setVisibility(View.GONE);
            container_phone.setVisibility(View.VISIBLE);
            viewContact.setVisibility(View.VISIBLE);
        }else{
            btn_finished.setVisibility(View.GONE);
            btn_accept.setVisibility(View.GONE);
            container_phone.setVisibility(View.GONE);
            viewContact.setVisibility(View.GONE);
        }

        if (demandService.getState() == 0){
            if (demandService.getNegociate() == 2){
                if (CurrentUser.getInstance().isPro()){
                    btn_accept.setVisibility(View.GONE);
                }else{
                    btn_accept.setVisibility(View.VISIBLE);
                }
            }else{
                if (CurrentUser.getInstance().isPro()){
                    btn_accept.setVisibility(View.VISIBLE);
                }else{
                    btn_accept.setVisibility(View.GONE);
                }
            }
        }

        if (CurrentUser.getInstance().isPro()){
            negociate = 2;
        }else{
            negociate = 1;
        }

        if (demandService.getOfficial_account() == 0){
            img_official_account.setVisibility(View.GONE);
        }else{
            img_official_account.setVisibility(View.VISIBLE);
        }

        if (demandService.getStatus() == 0){
            img_active_user.setColorFilter(ContextCompat.getColor(this, R.color.red));
            txt_active.setText("Inactive");
        }else{
            img_active_user.setColorFilter(ContextCompat.getColor(this, R.color.greenSeafoam));
            txt_active.setText("Active");
        }

    }

    @OnClick(R.id.btn_accept)
    public void accept_demand(){
        showBottomSheet();
    }

    @OnClick(R.id.btn_extend)
    public void extend_time(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(demandService.getDate_end()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        c.add(Calendar.DATE, 40);
        Date resultdate = new Date(c.getTimeInMillis());
        String date = sdf.format(resultdate);

        appApi.updateDateDemandService(date, demandService.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    if (!response.body().isError()){
                        Toast.makeText(DemandDetailsActivity.this, "La date de livraison a été modifié", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.btn_finished)
    public void finish_demand(){
        addCommission();
    }

    private void calculCommission() {
        if (CurrentUser.getInstance().getmCustomer().getPlan() == 101){
            commission = String.valueOf(demandService.getPrice() * 0.08);
        }else if (CurrentUser.getInstance().getmCustomer().getPlan() == 102){
            commission = String.valueOf(demandService.getPrice() * 0.06);
        }else if (CurrentUser.getInstance().getmCustomer().getPlan() == 103){
            commission = String.valueOf(demandService.getPrice() * 0.04);
        }else if (CurrentUser.getInstance().getmCustomer().getPlan() == 104){
            commission = String.valueOf(demandService.getPrice() * 0.02);
        }else{
            commission = String.valueOf(demandService.getPrice() * 0.10);
        }
    }

    private void addCommission() {
        show();
        appApi.addCommission(demandService.getId(), demandService.getPro_id(), demandService.getUser_id(), commission).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Demande terminé avec succés", Toast.LENGTH_SHORT).show();
                    finish();
                }
                hide();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hide();
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

    public void modifyState(int state){
        appApi.updateDemandState(state, demandService.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    if (!response.body().isError()){
                        demandService.setState(state);
                        Toast.makeText(DemandDetailsActivity.this, "Demande accepté avec succés", Toast.LENGTH_SHORT).show();
                        init();
                    }else{
                        Toast.makeText(DemandDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    public void showBottomSheet(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DemandDetailsActivity.this, R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_negociation);

        Button btn_cancel = bottomSheetDialog.findViewById(R.id.btn_cancel);
        Button btn_add = bottomSheetDialog.findViewById(R.id.btn_add);
        EditText et_date_start = bottomSheetDialog.findViewById(R.id.et_date_start);
        EditText et_date_final = bottomSheetDialog.findViewById(R.id.et_date_final);
        Spinner et_unité = bottomSheetDialog.findViewById(R.id.et_unité);
        EditText et_quantity = bottomSheetDialog.findViewById(R.id.et_quantity);
        EditText et_price = bottomSheetDialog.findViewById(R.id.et_price);
        EditText et_description = bottomSheetDialog.findViewById(R.id.et_description);
        Spinner spinner_services = bottomSheetDialog.findViewById(R.id.spinner_services);

        et_date_start.setText(demandService.getDate_start());
        et_date_final.setText(demandService.getDate_end());
        et_quantity.setText(String.valueOf(demandService.getQuantity()));
        et_price.setText(String.valueOf(demandService.getPrice()));
        et_description.setText(demandService.getDescription());
        btn_add.setText(R.string.accept);
        spinner_services.setVisibility(View.GONE);

        et_date_start.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(DemandDetailsActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            et_date_start.setText( year+"-" + (monthOfYear + 1) +"-" +dayOfMonth);
                        }
                    }, year, month, day);
            long now = System.currentTimeMillis();
            picker.getDatePicker().setMinDate(now);
            //  picker.getDatePicker().setMaxDate(now+(1000*60*60*24*40));
            picker.show();

        });

        et_date_final.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(DemandDetailsActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            et_date_final.setText( year+"-" + (monthOfYear + 1) +"-" +dayOfMonth);
                        }
                    }, year, month, day);
            long now = System.currentTimeMillis();
            picker.getDatePicker().setMinDate(now);
            //  picker.getDatePicker().setMaxDate(now+(1000*60*60*24*40));
            picker.show();

        });

        unities = Arrays.asList("cm", "m", "m²", "g", "Kg", "unité");

        int index = 0;
        for (String unity : unities){
            if (edt_unity.getText().toString().equals(unity)){
                index = unities.indexOf(unity);
            }
        }

        et_date_start.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(demandService.getDate_start())){
                    btn_add.setText("Négocier");
                }
            }
        });

        et_date_final.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(demandService.getDate_end())){
                    btn_add.setText("Négocier");
                }
            }
        });

        et_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0){
                    if (!editable.toString().matches(demandService.getDescription())){
                        btn_add.setText("Négocier");
                    }
                }
            }
        });

        et_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0){
                    if (Integer.parseInt(s.toString()) != demandService.getQuantity()){
                        btn_add.setText("Négocier");
                    }
                }

            }
        });

        et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (Integer.parseInt(s.toString()) != demandService.getPrice()) {
                        btn_add.setText("Négocier");
                    }
                }
            }
        });

        ArrayAdapter<String> adapter_unties = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adapter_unties.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_unité.setAdapter(adapter_unties);

        adapter_unties.addAll(unities);
        adapter_unties.notifyDataSetChanged();

        et_unité.setSelection(index);

        btn_add.setOnClickListener(v -> {
            if (btn_add.getText().toString().equals(getString(R.string.accept))){
                modifyState(1);
                bottomSheetDialog.dismiss();
                finish();
            }else{
                appApi.updateDemandService(et_date_start.getText().toString(), et_date_final.getText().toString(),
                        Integer.parseInt(et_quantity.getText().toString()), Integer.parseInt(et_price.getText().toString()),
                        demandService.getId(), negociate, et_unité.getSelectedItem().toString()).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()){
                            if (!response.body().isError()){
                                Toast.makeText(getApplicationContext(), "Négociation ajouté avec succés", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                    }
                });
            }
            //addNegociation(et_date_start.getText().toString(), et_date_final.getText().toString(), et_unité.getSelectedItem().toString(), et_quantity.getText().toString(), et_price.getText().toString(), et_description.getText().toString());
        });

        btn_cancel.setOnClickListener(v -> {
            modifyState(3);
            bottomSheetDialog.dismiss();
            Toast.makeText(DemandDetailsActivity.this, "la Demande est réfusée", Toast.LENGTH_SHORT).show();
            finish();
        });

        bottomSheetDialog.show();
    }

    public long printDifference(Date startDate, Date endDate) {

        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;

        return elapsedDays;
    }
}
package com.optim.bassit.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.models.HomeFeed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDemandServiceActivity extends BaseActivity {

    @BindView(R.id.switch_urgence)
    Switch switch_urgence;
    @BindView(R.id.et_date_start)
    EditText et_date_start;
    @BindView(R.id.et_date_end)
    EditText et_date_end;
    @BindView(R.id.sp_category)
    Spinner sp_category;
    @BindView(R.id.et_adrs)
    Spinner et_adrs;
    @BindView(R.id.et_unity)
    Spinner et_unity;
    @BindView(R.id.et_quantity)
    EditText et_quantity;
    @BindView(R.id.et_price)
    EditText et_price;
    @BindView(R.id.et_promocode)
    EditText et_promocode;
    @BindView(R.id.et_description)
    EditText et_description;
    @BindView(R.id.btn_demand)
    RelativeLayout btn_demand;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    AppApi appApi;

    ArrayList<String> list_cat;
    List<String> unities;
    List<Categorie> all;
    DatePickerDialog picker;
    List<String> cities, cities_ar;
    HomeFeed service;
    String category;
    List<String> cats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_demand_service);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);

        service = getIntent().getParcelableExtra("service");

        init();
    }

    private void init() {

        unities = Arrays.asList("cm", "m", "m²", "g", "Kg", "unité");

        ArrayAdapter<String> adapter_unties = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adapter_unties.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_unity.setAdapter(adapter_unties);

        adapter_unties.addAll(unities);
        adapter_unties.notifyDataSetChanged();


        ArrayAdapter<String> adapter_categorie = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adapter_categorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(adapter_categorie);

        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (all == null)
                    return;

                List<String> cats = new ArrayList<>();
                for (Categorie cat : all) {
                    String cat_designation = cat.getSous();
                    if (adapter_categorie.getItem(i).equals(cat.getCat()))
                        cats.add(cat_designation);
                }

                cats.add("");


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        show();
        appApi.getCategories().enqueue(new Callback<List<Categorie>>() {
            @Override
            public void onResponse(Call<List<Categorie>> call, Response<List<Categorie>> response) {
                hide();
                if (response.isSuccessful()) {
                    all = response.body();



                    for (Categorie cat : all) {
                        String cat_designation = cat.getCat();
                        if (!cats.contains(cat_designation))
                            cats.add(cat_designation);
                    }
                    adapter_categorie.clear();
                    cats.add("");
                    adapter_categorie.addAll(cats);
                    adapter_categorie.notifyDataSetChanged();


                        int pos = cats.indexOf(service.getCategorie());
                        sp_category.setSelection(pos);
                        category = service.getCategorie();

                }
            }

            @Override
            public void onFailure(Call<List<Categorie>> call, Throwable t) {
                hide();
                all = null;
                adapter_categorie.clear();
            }
        });

        et_date_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale.setDefault(Locale.FRANCE);
                final Calendar cldr = Calendar.getInstance();

                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddDemandServiceActivity.this,
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

            }
        });

        et_date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale.setDefault(Locale.FRANCE);
                final Calendar cldr = Calendar.getInstance();

                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddDemandServiceActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                et_date_end.setText( year+"-" + (monthOfYear + 1) +"-" +dayOfMonth);
                            }
                        }, year, month, day);
                long now = System.currentTimeMillis();

                picker.getDatePicker().setMinDate(now);
                //  picker.getDatePicker().setMaxDate(now+(1000*60*60*24*40));
                picker.show();

            }
        });

        cities = Arrays.asList("Adrar", "Chlef", "Laghouat", "Oum El Bouaghi", "Batna", "Béjaia", "Biskra", "Bechar", "Blida", "Bouira",
                "Tamanraset", "Tébessa", "Tlemcen", "Tiaret", "Tizi Ouzou", "Alger", "Djelfa", "Jijel", "Sétif", "Saida", "Skikda", "Sidi bel Abbés",
                "Annaba", "Guelma", "Constantine", "Médéa", "Mostaganem", "M'sila", "Mascara", "Ouargla", "Oran", "El Bayadh", "Ilizi", "Bordj bou Arréridj",
                "Boumerdes", "El tarf", "Tindouf", "Tissemsilt", "El Oued", "Khenchela", "Souk Ahras", "Tipaza", "Mila", "Ain Defla", "Naama", "Ain Temouchent",
                "Ghardaia", "Relizane", "El M'ghair", "El Meniaa", "Ouled Djellal", "Borj Baji Mokhtar", "Béni Abbas", "Timimoun", "Touggourt", "Djanet",
                "In Saleh", "In Guezzam");

        cities_ar = Arrays.asList("أدرار", "الشلف", "الأغواط", "أم البواقي", "باتنة", "بجاية", "بسكرة", "بشار", "البليدة", "البويرة",
                "تمنراست", "تبسة", "تلمسان", "تيارت", "تيزي وزو", "العاصمة", "الجلفة", "جيجل", "سطيف", "سعيدة", "سكيكدة", "سيدي بلعباس",
                "عنابة", "قالمة", "قسنطينة", "المدية", "مستغانم", "المسيلة", "معسكر", "ورقلة", "وهرا", "البيض", "إليزي", "برج بوعريريج",
                "بومرداس", "الطارف", "تندوف", "تيسيمسيلت", "الوادي", "خنشلة", "سوق أهراس", "تيبازة", "ميلة", "عين الدفلى", "النعامة", "عين تيموشنت",
                "غرداية", "غيليزان", "المغير", "المنيعة", "أولاد جلال", "برج باجي مختار", "بني عباس", "تيميمون", "تقرت", "جانت",
                "عين صالح", "عين قزام");

        ArrayAdapter<String> adapter_cities = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adapter_cities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_adrs.setAdapter(adapter_cities);


        adapter_cities.addAll(cities);
        adapter_cities.notifyDataSetChanged();

    }

    @OnClick(R.id.btn_demand)
    public void addDemand(){
        int urgence;


        String startDate, endDate;
        if (switch_urgence.isChecked()){
            urgence = 1;
        }else{
            urgence = 0;
        }

        if (et_date_start.getText().toString().matches("")){
            Toast.makeText(this, "Ajouter une date de début", Toast.LENGTH_SHORT).show();
            return;
        }else{
            startDate = et_date_start.getText().toString();
        }

        if (et_date_end.getText().toString().matches("")){
            Toast.makeText(this, "Ajouter une date de fin", Toast.LENGTH_SHORT).show();
            return;
        }else{
            endDate = et_date_end.getText().toString();
        }

        if (et_quantity.getText().toString().matches("")){
            Toast.makeText(this, "Ajouter une quantité", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (et_price.getText().toString().matches("")){
//            Toast.makeText(this, "Ajouter un prix", Toast.LENGTH_SHORT).show();
//            return;
//        }

        show();
        appApi.addDemandService(CurrentUser.getInstance().getmCustomer().getId(), service.getId(), startDate, endDate,
                sp_category.getSelectedItem().toString(), et_adrs.getSelectedItem().toString(), urgence,
                et_promocode.getText().toString(), et_unity.getSelectedItem().toString(), Integer.parseInt(et_quantity.getText().toString()),
                et_price.getText().toString(), et_description.getText().toString(), service.getUser_id(), 1).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                hide();
                if (response.isSuccessful()){
                    Toast.makeText(AddDemandServiceActivity.this, "Demande crée avec succés", Toast.LENGTH_SHORT).show();
                    finish();
                }
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

}
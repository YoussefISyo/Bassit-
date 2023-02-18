package com.optim.bassit.ui.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.models.HomeadsFeed;
import com.optim.bassit.models.Service;
import com.optim.bassit.ui.adapters.TagAdapter;
import com.optim.bassit.utils.OptimTools;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdsAddEditActivity extends BaseActivity {




    private boolean isNew;

    @BindView(R.id.et_title)
    EditText tTitle;
    @BindView(R.id.et_date)
    EditText tdate;
    @BindView(R.id.et_adrs)
    Spinner tadrs;
    @BindView(R.id.et_description)
    EditText tDescription;
    @BindView(R.id.sp_category)
    Spinner spCategory;
    @BindView(R.id.switch_urgence)
    Switch urgenceSwitch;
    @BindView(R.id.et_date_start)
    EditText edtStartDate;
    @BindView(R.id.et_unity)
    Spinner unitySpinner;
    @BindView(R.id.et_quantity)
    EditText quantityEdit;
    @BindView(R.id.et_price)
    EditText edtPrice;


    @Inject
    AppApi appApi;
    HomeadsFeed mHomeFeed;
    ArrayList<String> list_cat;
    DatePickerDialog picker;
    List<String> cities, cities_ar;
    List<String> unities;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_add_edit);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);



        tdate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Locale.setDefault(Locale.FRANCE);
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(AdsAddEditActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            tdate.setText( year+"-" + (monthOfYear + 1) +"-" +dayOfMonth);
                        }
                    }, year, month, day);
            long now = System.currentTimeMillis();
            picker.getDatePicker().setMinDate(now);
          //  picker.getDatePicker().setMaxDate(now+(1000*60*60*24*40));
            picker.show();

        }
    });
        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale.setDefault(Locale.FRANCE);
                final Calendar cldr = Calendar.getInstance();
                int dayStart = cldr.get(Calendar.DAY_OF_MONTH);
                int monthStart = cldr.get(Calendar.MONTH);
                int yearStart = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AdsAddEditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtStartDate.setText( year+"-" + (monthOfYear + 1) +"-" +dayOfMonth);
                            }
                        }, yearStart, monthStart, dayStart);
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
        tadrs.setAdapter(adapter_cities);

        adapter_cities.addAll(cities);
        adapter_cities.notifyDataSetChanged();

        unities = Arrays.asList("cm", "m", "m²", "g", "Kg", "unité");

        ArrayAdapter<String> adapter_unties = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adapter_unties.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitySpinner.setAdapter(adapter_unties);

        adapter_unties.addAll(unities);
        adapter_unties.notifyDataSetChanged();

        ArrayAdapter<String> adapter_categorie = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adapter_categorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter_categorie);


        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               /* if (all == null)
                    return;

                List<String> cats = new ArrayList<>();
                for (Categorie cat : all) {
                    String cat_designation = cat.getSous();
                    if (adapter_categorie.getItem(i).equals(cat.getCat()))
                        cats.add(cat_designation);
                }

                cats.add("");*/


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (getIntent().getExtras() != null) {
            isNew = getIntent().getExtras().getBoolean("isnew");
            if (!isNew)
                mHomeFeed = getIntent().getParcelableExtra("parcel_service");
        }

        if (mHomeFeed != null) {
            EditText title = findViewById(R.id.et_title);
            EditText description = findViewById(R.id.et_description);

            title.setText(mHomeFeed.getTitle());
            description.setText(mHomeFeed.getDes());



        }


        show();
        appApi.getCategories().enqueue(new Callback<List<Categorie>>() {
            @Override
            public void onResponse(Call<List<Categorie>> call, Response<List<Categorie>> response) {
                hide();
                if (response.isSuccessful()) {
                    all = response.body();
                    List<String> cats = new ArrayList<>();
                    cats_id = new ArrayList<>();

                    for (Categorie cat : all) {
                        String cat_designation = cat.getCat();

                        if (!cats.contains(cat_designation)) {
                            cats.add(cat_designation);
                            cats_id.add(cat.getCat_id()+"");
                        }
                    }
                    adapter_categorie.clear();
                    cats.add("");
                    adapter_categorie.addAll(cats);
                    adapter_categorie.notifyDataSetChanged();

                 /*   if (!isNew) {

                        int pos = cats.indexOf(mHomeFeed.getCategorie());
                        spCategory.setSelection(pos);
                    }
                    if (bafrili)
                        spCategory.setSelection(0);*/
                }
            }

            @Override
            public void onFailure(Call<List<Categorie>> call, Throwable t) {
                hide();
                all = null;
                adapter_categorie.clear();
                new android.app.AlertDialog.Builder(AdsAddEditActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(getString(R.string.noconnection))
                        .setPositiveButton(R.string.text_ok, (a, b) -> {
                            finish();
                            a.dismiss();
                        }).show();
            }
        });


    }

    boolean bsous;
    List<Categorie> all;
    List<String> cats_id = new ArrayList<>();



    @OnClick({R.id.b_confirmer})
    public void onConfirmerClick() {



        String title = tTitle.getText().toString();
        String description = tDescription.getText().toString();
        String date = tdate.getText().toString();
        String dateStart = edtStartDate.getText().toString();
        String price = edtPrice.getText().toString();
        String adrs = tadrs.getSelectedItem().toString();
        String unity = unitySpinner.getSelectedItem().toString();
        int quantity = 0;
        if (!quantityEdit.getText().toString().isEmpty()){
             quantity = Integer.parseInt(quantityEdit.getText().toString());
        }

        int urgence = 0;
        if (urgenceSwitch.isChecked()){
            urgence = 1;
        }

        if (title.matches("") || description.matches("") || adrs.trim().equals("")|| date.trim().equals("") || quantity == 0) {
            Alert(R.string.message_fill_required_fields);
            return;
        }

        show();
        appApi.addEditAds(CurrentUser.getInstance().getmCustomer().getId()+"",cats_id.get(spCategory.getSelectedItemPosition()),title,description,date,adrs,
                urgence, dateStart, unity, quantity, price)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        hide();
                        if (!response.isSuccessful() || response.body().isError()) {
                            Alert(response.body().getMessage());
                        } else {
                            AdsAddEditActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        hide();
                        Alert(t.getMessage());

                    }
                });

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

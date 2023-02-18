package com.optim.bassit.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.models.Customer;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.models.HomeadsFeed;
import com.optim.bassit.models.Service;
import com.optim.bassit.ui.adapters.TagAdapter;
import com.optim.bassit.utils.OptimTools;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class EditServiceActivity extends BaseActivity {

    @BindView(R.id.edt_title)
    EditText edt_title;

    @BindView(R.id.edt_description)
    EditText edt_description;

    @BindView(R.id.rvTags)
    RecyclerView rvTags;
    @BindView(R.id.sp_category)
    Spinner spCategory;
    @BindView(R.id.sp_souscategory)
    Spinner spSousCategory;
    @BindView(R.id.et_unité)
    Spinner et_unité;
    @BindView(R.id.et_price)
    EditText et_price;
    @BindView(R.id.et_navigui)
    EditText et_navigui;
    @BindView(R.id.txt_switch)
    TextView txt_switch;
    @BindView(R.id.switch_pause)
    Switch switch_pause;
    @BindView(R.id.img_featured)
    ImageView img_featured;
    @BindView(R.id.b_back)
    ImageButton b_back;

    TagAdapter tag_adapter;
    ArrayList<String> list_cat;
    List<String> unities;
    List<Categorie> all;

    @Inject
    AppApi appApi;

    Boolean bafrili;
    HomeFeed mHomeFeed;
    ArrayAdapter<String> adapter_souscategorie;
    boolean bsous;
    String featured_path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);
        ButterKnife.bind(this);
        App.getApp(this.getApplication()).getComponent().inject(this);

        mHomeFeed = getIntent().getParcelableExtra("parcel_service");
        bafrili = CurrentUser.getInstance().getmCustomer().getTherole() == 33;

        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {

        edt_title.setText(mHomeFeed.getTitle());
        edt_description.setText(mHomeFeed.getDescription());
        et_price.setText(mHomeFeed.getMin_price());
        et_navigui.setText(String.valueOf(mHomeFeed.getGagner()));
        OptimTools.getPicasso(mHomeFeed.getServiceImageLink(800)).into(img_featured);

        b_back.setOnClickListener(view -> finish());




        if (bafrili) {
            spCategory.setVisibility(View.GONE);
            spSousCategory.setVisibility(View.GONE);
            findViewById(R.id.h_cat).setVisibility(View.GONE);
            findViewById(R.id.h_sous).setVisibility(View.GONE);
            findViewById(R.id.b_ask_new).setVisibility(View.GONE);
        }

        if (mHomeFeed.getPause() == 1) {
            txt_switch.setText(R.string.activer_mes_services);
            switch_pause.setChecked(false);
        }else{
            txt_switch.setText(R.string.pause_mes_services);
            switch_pause.setChecked(true);
        }


        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        rvTags.setLayoutManager(layoutManager);

        tag_adapter = new TagAdapter(true, tag -> tag_adapter.removeOne(tag));
        rvTags.setAdapter(tag_adapter);

        if (mHomeFeed.getTags() != null) {
            List<String> tags = new ArrayList<String>(Arrays.asList(mHomeFeed.getTags().split(";")));
            tag_adapter.fill(tags);
        }

        unities = Arrays.asList("cm", "m", "m²", "g", "Kg", "unité");

        ArrayAdapter<String> adapter_unties = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adapter_unties.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_unité.setAdapter(adapter_unties);

        adapter_unties.addAll(unities);
        adapter_unties.notifyDataSetChanged();

        ArrayAdapter<String> adapter_categorie = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adapter_categorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter_categorie);

        adapter_souscategorie = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adapter_souscategorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSousCategory.setAdapter(adapter_souscategorie);

        featured_path = "*";
        OptimTools.getPicasso(mHomeFeed.getServiceImageLink(800)).into(img_featured);

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                adapter_souscategorie.clear();
                cats.add("");
                adapter_souscategorie.addAll(cats);
                adapter_souscategorie.notifyDataSetChanged();

                    if (!bsous) {
                        bsous = true;
                        try {
                            int pos = cats.indexOf(mHomeFeed.getSouscategorie());
                            spSousCategory.setSelection(pos);
                        } catch (Exception ex) {

                        }

                    }



                if (bafrili)
                    spSousCategory.setSelection(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapter_souscategorie.clear();
                adapter_souscategorie.notifyDataSetChanged();
            }
        });

        show();
        appApi.getCategories().enqueue(new Callback<List<Categorie>>() {
            @Override
            public void onResponse(Call<List<Categorie>> call, Response<List<Categorie>> response) {
                hide();
                if (response.isSuccessful()) {
                    all = response.body();
                    List<String> cats = new ArrayList<>();


                    for (Categorie cat : all) {
                        String cat_designation = cat.getCat();
                        if (bafrili && !cat_designation.equals("Divers"))
                            continue;
                        if (!cats.contains(cat_designation))
                            cats.add(cat_designation);
                    }
                    adapter_categorie.clear();
                    cats.add("");
                    adapter_categorie.addAll(cats);
                    adapter_categorie.notifyDataSetChanged();


                    int pos = cats.indexOf(mHomeFeed.getCategorie());
                    spCategory.setSelection(pos);

                    if (bafrili)
                        spCategory.setSelection(0);
                }
            }

            @Override
            public void onFailure(Call<List<Categorie>> call, Throwable t) {
                hide();
                all = null;
                adapter_categorie.clear();
            }
        });

        switch_pause.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch_pause.setClickable(false);
                PauseService();
                return false;
            }
        });
    }

    @OnClick({R.id.b_ask_new})
    public void onAskNew() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(R.string.demande_new_cat);
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton(R.string.text_envoyer, (dialog, which) -> {
            String s = input.getText().toString();
            if (s.matches("")) {
                Alert(R.string.veuillez_saisir_votre_proposition);
                return;
            }

            OptimTools.hideKeyboard(this);


            fullyHandleResponseSuccessOnly(appApi.askNew(s), () -> {
                Alert(R.string.your_request_have_been_sent);
            });

            dialog.dismiss();

        });
        builder.show();

        input.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }

    public void PauseService() {

        AlertYesNo(mHomeFeed.getPause() == 1 ? getString(R.string.service_pause_confirmation) : getString(R.string.service_activate_confirmation), getString(mHomeFeed.getPause() == 1 ?
                        R.string.text_activer : R.string.text_pause),
                getString(R.string.text_cancel), () -> {

                    show();

                    fullyHandleResponseSuccessOnly(appApi.pauseService(mHomeFeed.getId()), () -> {
                        if (mHomeFeed.getPause() == 1){
                            switch_pause.setChecked(true);
                        }else{
                            switch_pause.setChecked(false);
                        }
                        hide();
                        finish();
                    });

                });


    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        finish();
    }

    @OnClick({R.id.btn_confirmer})
    public void onConfirmerClick() {

        if (!et_navigui.getText().toString().matches(""))
            if (Integer.valueOf(et_navigui.getText().toString()) < 100) {
                Alert(getString(R.string.montant_min_pg) + " 100" + getString(R.string.text_da));
                return;
            }

        String title = edt_title.getText().toString();
        String description = edt_description.getText().toString();
        String min = et_price.getText().toString();
        Object sp_sous = spSousCategory.getSelectedItem();
        if (title.matches("") || description.matches("")) {
            Alert(R.string.message_fill_required_fields);
            return;
        }
        if (min.trim().isEmpty()) {
            Alert(R.string.message_fill_required_fields);
            return;
        }
        if (!bafrili && sp_sous == null) {
            Alert(R.string.message_fill_required_fields);
            return;
        }

        if (featured_path == null || featured_path.matches("")) {
            Alert(R.string.please_select);
            return;
        }

        int sous_id = 0;
        String tags = "";
        for (String ss : tag_adapter.getList()) {
            tags += ss + ";";
        }

        if (bafrili) {
            sous_id = -1;
        } else {

            for (Categorie ii : all) {
                String cat = spCategory.getSelectedItem().toString();
                String sous = spSousCategory.getSelectedItem().toString();
                if (cat.equals(ii.getCat()) && sous.equals(ii.getSous())) {
                    sous_id = ii.getSous_id();
                    break;
                }
            }

            if (sous_id == 0) {
                Alert(R.string.message_fill_required_fields);
                return;
            }
        }

        Service service = new Service();
        service.setTitle(edt_title.getText().toString());
        service.setDescription(edt_description.getText().toString());
        service.setSouscategorie_id(sous_id);
        service.setMin_price(min);

        if (!et_navigui.getText().toString().matches(""))
            service.setGagner(Integer.valueOf(et_navigui.getText().toString()));
        service.setTag(tags);


        MultipartBody.Part imageRequest = prepareFilePart("featured", featured_path);
        Customer cus = CurrentUser.getInstance().getmCustomer();

        show();
        appApi.editService(cus.getFullName(), cus.getCity(), cus.getBwilaya(), service.getSouscategorie_id(), service.getMin_price(), service.getTitle(), service.getDescription(), service.getTag(), mHomeFeed == null ? 0 : mHomeFeed.getId(), service.getGagner(), imageRequest)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        hide();
                        if (!response.isSuccessful() || response.body().isError()) {
                            Toast.makeText(EditServiceActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditServiceActivity.this, "Le service est modifié avec succés", Toast.LENGTH_SHORT).show();
                            EditServiceActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        hide();
                        Alert(t.getMessage());

                    }
                });


    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String fileUri) {

        if (fileUri.equals("*")) {
            return MultipartBody.Part.createFormData(partName, fileUri);
        } else {
            File file1 = new File(fileUri);
            File file = OptimTools.resizeFile(file1, 1000, this);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        }
        // MultipartBody.Part is used to send also the actual file name

    }

    @OnClick({R.id.img_help_navigui})
    public void showDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(EditServiceActivity.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_navigui, null);
        builder.setView(customLayout);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        customLayout.findViewById(R.id.btn_ok).setOnClickListener(v1 -> {
            dialog.dismiss();
        });
    }

}
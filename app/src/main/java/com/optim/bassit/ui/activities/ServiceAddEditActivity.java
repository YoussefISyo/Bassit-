package com.optim.bassit.ui.activities;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.models.Customer;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.models.Service;
import com.optim.bassit.ui.adapters.TagAdapter;
import com.optim.bassit.utils.OptimTools;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;

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

import static com.optim.bassit.utils.OptimTools.Alert;

public class ServiceAddEditActivity extends BaseActivity {

    TagAdapter tag_adapter;
    @BindView(R.id.img_featured)
    ImageView img_featured;

    private boolean isNew;

    @BindView(R.id.et_title)
    EditText tTitle;
    @BindView(R.id.t_gagner)
    EditText t_gagner;

    @BindView(R.id.et_min)
    EditText tmin;

    @BindView(R.id.et_description)
    EditText tDescription;
    @BindView(R.id.sp_category)
    Spinner spCategory;
    @BindView(R.id.sp_souscategory)
    Spinner spSousCategory;

    @BindView(R.id.tags_layout)
    LinearLayout tagsLayout;

    String featured_path = "";

    @Inject
    AppApi appApi;
    boolean bafrili;
    List<MultipartBody.Part> servicePhotos;
    HomeFeed mHomeFeed;
    ArrayList<String> list_cat;
    List<String> unities;

    @BindView(R.id.et_unité)
    Spinner et_unité;

    private ArrayAdapter<String> adapter_souscategorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_add_edit);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        bafrili = CurrentUser.getInstance().getmCustomer().getTherole() == 33;
        RecyclerView list_tags = findViewById(R.id.rvTags);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        list_tags.setLayoutManager(layoutManager);

        tag_adapter = new TagAdapter(true, tag -> tag_adapter.removeOne(tag));
        list_tags.setAdapter(tag_adapter);
        servicePhotos = new ArrayList<>();

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

        if (bafrili) {
            spCategory.setVisibility(View.GONE);
            spSousCategory.setVisibility(View.GONE);
            findViewById(R.id.h_cat).setVisibility(View.GONE);
            findViewById(R.id.h_sous).setVisibility(View.GONE);
            findViewById(R.id.b_ask_new).setVisibility(View.GONE);
        }

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
                if (!isNew) {
                    if (!bsous) {
                        bsous = true;
                        try {
                            int pos = cats.indexOf(mHomeFeed.getSouscategorie());
                            spSousCategory.setSelection(pos);
                        } catch (Exception ex) {

                        }

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

        if (getIntent().getExtras() != null) {
            isNew = getIntent().getExtras().getBoolean("isnew");
            if (!isNew)
                mHomeFeed = getIntent().getParcelableExtra("parcel_service");
        }

        if (mHomeFeed != null) {
            EditText title = findViewById(R.id.et_title);
            EditText description = findViewById(R.id.et_description);
            EditText min = findViewById(R.id.et_min);
            title.setText(mHomeFeed.getTitle());
            min.setText(mHomeFeed.getMin_price());
            t_gagner.setText(mHomeFeed.getGagner() + "");
            description.setText(mHomeFeed.getDescription());

            if (mHomeFeed.getTags() != null) {
                List<String> tags = new ArrayList<String>(Arrays.asList(mHomeFeed.getTags().split(";")));
                tag_adapter.fill(tags);
            }

            if (mHomeFeed.getPause() == 1) {
                Button b_pause = findViewById(R.id.b_pause);
                b_pause.setText(R.string.activer_mon_service);
                b_pause.setBackground(getResources().getDrawable(R.drawable.round_button));
            }

            featured_path = "*";
            OptimTools.getPicasso(mHomeFeed.getServiceImageLink(800)).into(img_featured);
        }


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

                    if (!isNew) {

                        int pos = cats.indexOf(mHomeFeed.getCategorie());
                        spCategory.setSelection(pos);
                    }
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

        if (mHomeFeed != null) {
            appApi.canDelete(mHomeFeed.getId()).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && Integer.valueOf(response.body().getMessage()) == 0)
                        findViewById(R.id.b_delete).setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {

                }
            });


        } else {
            findViewById(R.id.b_pause).setVisibility(View.GONE);
        }
    }

    boolean bsous;
    List<Categorie> all;

    @OnClick({R.id.b_add_img})
    public void AddImage() {
        PickImageDialog.build(new PickSetup()
                .setTitle(getString(R.string.t_une_photo_pour_le_service))
                .setTitleColor(R.color.colorPrimaryDark)
                .setGalleryButtonText(getString(R.string.galerie))
                .setCameraButtonText(getString(R.string.camera))
                .setCancelText(this.getString(R.string.text_cancel))
                .setPickTypes(EPickType.GALLERY, EPickType.CAMERA)
                .setButtonOrientation(LinearLayout.HORIZONTAL)
                .setSystemDialog(false))
                .setOnPickResult(r -> {
                    if (r.getError() == null) {

                        File imgFile = new File(r.getPath());

                        if (imgFile.exists()) {
                            try {
                                featured_path = imgFile.getAbsolutePath();
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                              //  img_featured.setImageBitmap(myBitmap);

                                Glide.with(this).load(myBitmap).into(img_featured);
                            }catch (RuntimeException e){
                                Toast.makeText(this, "Try another image", Toast.LENGTH_SHORT).show();
                            }
                            
                        }

                    } else {
                        Alert(r.getError().getMessage());
                    }
                })
                .setOnPickCancel(() -> finish()).show(this.getSupportFragmentManager());
    }


    @OnClick(R.id.b_add_tag)
    public void addTag() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, list_cat);
        AutoCompleteTextView textView = new AutoCompleteTextView(this);
        textView.setAdapter(adapter);

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_tag))
                .setView(textView)
                .setMessage(R.string.tags_separated_by)
                .setCancelable(false)
                .setPositiveButton(R.string.add, (dialog, id) -> {
                    String s = textView.getText().toString();
                    for (String sz : s.replace("+", ";").replace(",", ";").split(";")) {
                        tag_adapter.addOne(sz);
                    }
                    dialog.dismiss();
                }).setNegativeButton(R.string.text_cancel, (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }

    @OnClick({R.id.b_confirmer})
    public void onConfirmerClick() {

        if (!t_gagner.getText().toString().matches(""))
            if (Integer.valueOf(t_gagner.getText().toString()) < 100) {
                Alert(getString(R.string.montant_min_pg) + " 100" + getString(R.string.text_da));
                return;
            }

        String title = tTitle.getText().toString();
        String description = tDescription.getText().toString();
        String min = tmin.getText().toString();
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
        service.setTitle(tTitle.getText().toString());
        service.setDescription(tDescription.getText().toString());
        service.setSouscategorie_id(sous_id);
        service.setMin_price(min);

        if (!t_gagner.getText().toString().matches(""))
            service.setGagner(Integer.valueOf(t_gagner.getText().toString()));
        service.setTag(tags);


        MultipartBody.Part imageRequest = prepareFilePart("featured", featured_path);
        Customer cus = CurrentUser.getInstance().getmCustomer();

        show();
        appApi.addEditService(cus.getFullName(), cus.getCity(), cus.getBwilaya(), service.getSouscategorie_id(), service.getMin_price(), service.getTitle(), service.getDescription(), service.getTag(), mHomeFeed == null ? 0 : mHomeFeed.getId(), service.getGagner(), imageRequest)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        hide();
                        if (!response.isSuccessful() || response.body().isError()) {
                            Alert(response.body().getMessage());
                        } else {
                            AlertWait(getString(R.string.service_accept_admin), () -> {
                                ServiceAddEditActivity.this.finish();
                            });

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

//    @OnClick({R.id.b_cancel})
//    public void onAnnulerClick() {
//        this.finish();
//    }

    @OnClick({R.id.img_help_navigui})
    public void showDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ServiceAddEditActivity.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_navigui, null);
        builder.setView(customLayout);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        customLayout.findViewById(R.id.btn_ok).setOnClickListener(v1 -> {
            dialog.dismiss();
        });
    }

    @OnClick({R.id.b_continuer})
    public void onNext() {
        findViewById(R.id.container_firstInfo).setVisibility(View.GONE);
        findViewById(R.id.container_secondInfo).setVisibility(View.VISIBLE);
        findViewById(R.id.b_confirmer).setVisibility(View.VISIBLE);
        findViewById(R.id.b_continuer).setVisibility(View.GONE);
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

    @OnClick({R.id.b_pause})
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
    }
}

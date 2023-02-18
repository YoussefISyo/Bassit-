package com.optim.bassit.ui.activities;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.AppData;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.models.SousCategorie;
import com.optim.bassit.ui.adapters.CatsAdapter;
import com.optim.bassit.ui.adapters.RechercheSousCategorieAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends BaseActivity implements CatsAdapter.ItemClickListener, RechercheSousCategorieAdapter.ItemClickListener {

    CatsAdapter adapterCats;
    RechercheSousCategorieAdapter adapterSousCategories;
    Dialog myDialog;

    Double longitude;
    Double latitude;

    @Inject
    AppApi appApi;
    @BindView(R.id.edt_textSearch)
    EditText edt_textSearch;
    @BindView(R.id.btnSearch)
    RelativeLayout btnSearch;
    @BindView(R.id.price_slider)
    RangeSlider price_slider;
    @BindView(R.id.rating_bar)
    RatingBar rating_bar;
    @BindView(R.id.switch_official_account)
    Switch switch_official_account;
    @BindView(R.id.vue_under50)
    CheckedTextView vue_under50;
    @BindView(R.id.vue_under100)
    CheckedTextView vue_under100;
    @BindView(R.id.vue_above100)
    CheckedTextView vue_above100;
    @BindView(R.id.vue_above200)
    CheckedTextView vue_above200;
    @BindView(R.id.cancel_vues)
    ImageView cancel_vues;
    @BindView(R.id.imgDeleteNotation)
    ImageView imgDeleteNotation;
    @BindView(R.id.cancel_price)
    ImageView cancel_price;
    @BindView(R.id.txt_position)
    TextView txt_position;

    Integer min_price = 0;
    Integer official_account = 0;
    Integer max_price = 100000;
    String vues = "10000";
    String operator = "<";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        myDialog = new Dialog(this);

        init();
    }

    private void init() {
        RecyclerView lst_cats = findViewById(R.id.lst_cats);
        lst_cats.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterCats = new CatsAdapter(this);
        adapterCats.fill(AppData.getInstance().getCategories());
        lst_cats.setAdapter(adapterCats);

        RecyclerView recyclerViewHr = findViewById(R.id.lst_kind);
        recyclerViewHr.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterSousCategories = new RechercheSousCategorieAdapter(this);
        adapterSousCategories.fill(AppData.getInstance().getSousCategories());
        recyclerViewHr.setAdapter(adapterSousCategories);

        price_slider.setLabelFormatter(value -> String.format("%.0f", value) + " DA");

        price_slider.addOnChangeListener((slider, value, fromUser) -> {
            min_price = slider.getValues().get(0).intValue();
            max_price = slider.getValues().get(1).intValue();

        });

        vue_under50.setOnClickListener(v -> {
            if (vue_under50.isChecked()){
                vue_under50.setChecked(false);

            }else{
                vue_under50.setChecked(true);
                vue_under100.setChecked(false);
                vue_above100.setChecked(false);
                vue_above200.setChecked(false);
                vues = "50";
                operator = "<";
            }
        });

        vue_under100.setOnClickListener(v -> {
            if (vue_under100.isChecked()){
                vue_under100.setChecked(false);
            }else{
                vue_under100.setChecked(true);
                vue_under50.setChecked(false);
                vue_above100.setChecked(false);
                vue_above200.setChecked(false);
                vues = "100";
                operator = "<";
            }
        });

        vue_above100.setOnClickListener(v -> {
            if (vue_above100.isChecked()){
                vue_above100.setChecked(false);
            }else{
                vue_above100.setChecked(true);
                vue_under50.setChecked(false);
                vue_under100.setChecked(false);
                vue_above200.setChecked(false);
                vues = "100";
                operator = ">";
            }
        });

        vue_above200.setOnClickListener(v -> {
            if (vue_above200.isChecked()){
                vue_above200.setChecked(false);
            }else{
                vue_above200.setChecked(true);
                vue_under50.setChecked(false);
                vue_above100.setChecked(false);
                vue_under100.setChecked(false);
                vues = "200";
                operator = ">";
            }
        });

        cancel_vues.setOnClickListener(v -> {
            vue_under50.setChecked(false);
            vue_above100.setChecked(false);
            vue_under100.setChecked(false);
            vue_above200.setChecked(false);
        });

        imgDeleteNotation.setOnClickListener(v -> {
          rating_bar.setRating(0);
        });

        cancel_price.setOnClickListener(v -> {
            price_slider.setValues(0F, 100000F);
        });

        txt_position.setOnClickListener(v -> {
            Intent intent = new Intent(FilterActivity.this, MapSearchActivity.class);
            intent.putExtra("user", true);
            startActivityForResult(intent, 104);
        });



    }

    @Override
    public void onItemClick(Categorie cat) {
        onSelectCategory(cat.getId());
    }

    private void onSelectCategory(int cat) {
        adapterCats.toggleSelected(cat);
        updateSousCategories();
    }

    @Override
    public void onItemClick(SousCategorie cat) {
        adapterSousCategories.toggleSelected(cat.getId());
    }

    private void updateSousCategories() {

        adapterSousCategories.toggleSelected(-1);
        int id = adapterCats.selectedCat();
        if (id == -1) {
            adapterSousCategories.fill(AppData.getInstance().getSousCategories());
            return;
        }
        List<SousCategorie> scs = new ArrayList<>();
        for (SousCategorie ki : AppData.getInstance().getSousCategories()) {
            if (ki.getCategorie_id() == id)
                scs.add(ki);
        }
        adapterSousCategories.fill(scs);
    }

    private void getSearchHome(Integer page) {

        if (switch_official_account.isChecked()){
            official_account = 1;
        }else{
            official_account = 0;
        }
        Intent intent = new Intent(FilterActivity.this, SearchResultActivity.class);
        show();
        if (rating_bar.getRating() == 0F){
            if (txt_position.getText().toString().matches("")){
                appApi.getSearchHomeWithoutRating(adapterSousCategories.selectedSousCategorie(), adapterCats.selectedCat(),
                        edt_textSearch.getText().toString(), page, min_price, max_price,
                        official_account, vues, operator).enqueue(new Callback<List<HomeFeed>>() {
                    @Override
                    public void onResponse(Call<List<HomeFeed>> call, Response<List<HomeFeed>> response) {
                        if (response.isSuccessful()){
                            Log.d("search", response.body().toString());
                            intent.putParcelableArrayListExtra("list_search", (ArrayList<? extends Parcelable>) response.body());
                            startActivity(intent);
                            hide();
                        }

                        // adapterresult.fill(page, response.body());
                        //swipe.setRefreshing(false);

                    }

                    @Override
                    public void onFailure(Call<List<HomeFeed>> call, Throwable t) {
                        // swipe.setRefreshing(false);
                        hide();
                    }
                });
            }else{
                appApi.getSearchHomePositionWithoutRating(adapterSousCategories.selectedSousCategorie(), adapterCats.selectedCat(),
                        edt_textSearch.getText().toString(), page, min_price, max_price,
                        official_account, vues, operator, longitude, latitude).enqueue(new Callback<List<HomeFeed>>() {
                    @Override
                    public void onResponse(Call<List<HomeFeed>> call, Response<List<HomeFeed>> response) {
                        if (response.isSuccessful()){
                            Log.d("search", response.body().toString());
                            intent.putParcelableArrayListExtra("list_search", (ArrayList<? extends Parcelable>) response.body());
                            startActivity(intent);
                            hide();
                        }

                        // adapterresult.fill(page, response.body());
                        //swipe.setRefreshing(false);

                    }

                    @Override
                    public void onFailure(Call<List<HomeFeed>> call, Throwable t) {
                        // swipe.setRefreshing(false);
                        hide();
                    }
                });
            }

        }else{
            if (txt_position.getText().toString().matches("")){
                appApi.getSearchHome(adapterSousCategories.selectedSousCategorie(), adapterCats.selectedCat(),
                        edt_textSearch.getText().toString(), page, min_price, max_price,
                        official_account, vues, operator, (int)rating_bar.getRating()).enqueue(new Callback<List<HomeFeed>>() {
                    @Override
                    public void onResponse(Call<List<HomeFeed>> call, Response<List<HomeFeed>> response) {
                        if (response.isSuccessful()){
                            Log.d("search", response.body().toString());
                            intent.putParcelableArrayListExtra("list_search", (ArrayList<? extends Parcelable>) response.body());
                            startActivity(intent);
                            hide();
                        }

                        // adapterresult.fill(page, response.body());
                        //swipe.setRefreshing(false);

                    }

                    @Override
                    public void onFailure(Call<List<HomeFeed>> call, Throwable t) {
                        // swipe.setRefreshing(false);
                        hide();
                    }
                });
            }else{
                appApi.getSearchHomePosition(adapterSousCategories.selectedSousCategorie(), adapterCats.selectedCat(),
                        edt_textSearch.getText().toString(), page, min_price, max_price,
                        official_account, vues, operator, (int)rating_bar.getRating(), longitude, latitude).enqueue(new Callback<List<HomeFeed>>() {
                    @Override
                    public void onResponse(Call<List<HomeFeed>> call, Response<List<HomeFeed>> response) {
                        if (response.isSuccessful()){
                            Log.d("search", response.body().toString());
                            intent.putParcelableArrayListExtra("list_search", (ArrayList<? extends Parcelable>) response.body());
                            startActivity(intent);
                            hide();
                        }

                        // adapterresult.fill(page, response.body());
                        //swipe.setRefreshing(false);

                    }

                    @Override
                    public void onFailure(Call<List<HomeFeed>> call, Throwable t) {
                        // swipe.setRefreshing(false);
                        hide();
                    }
                });
            }

        }


    }

    @OnClick(R.id.btnSearch)
    public void btn_search() {
        getSearchHome(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 104){
            if (resultCode == RESULT_OK){
                 longitude = data.getDoubleExtra("longitude", 0);
                 latitude = data.getDoubleExtra("latitude", 0);
                 String address = data.getStringExtra("address");

                txt_position.setText(address);
            }
        }
    }
}
package com.optim.bassit.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Album;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Avis;
import com.optim.bassit.models.Customer;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.models.Photo;
import com.optim.bassit.ui.adapters.AlbumAdapter;
import com.optim.bassit.ui.adapters.AvisAdapter;
import com.optim.bassit.ui.adapters.TagAdapter;
import com.optim.bassit.utils.OptimTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceActivity extends BaseActivity implements AlbumAdapter.ItemClickListener {

    AvisAdapter adapter_two;

    @BindView(R.id.lst_albums)
    RecyclerView lst_albums;

    @BindView(R.id.img_featured)
    ImageView img_featured;

    @BindView(R.id.t_city)
    TextView t_city;
    @BindView(R.id.t_count)
    TextView t_count;

    @BindView(R.id.b_edit)
    ImageButton Edit_btn;

    @BindView(R.id.b_gagner)
    RelativeLayout b_gagner;

    @BindView(R.id.background_card)
    ImageView background_card;

    @BindView(R.id.txtUnityDistance)
    TextView txtUnityDistance;

    @BindView(R.id.btn_upgrade)
    RelativeLayout btn_upgrade;
    @BindView(R.id.img_active_user)
    ImageView img_active_user;
    @BindView(R.id.txt_active)
    TextView txt_active;
    @BindView(R.id.container_official)
    LinearLayout container_official;

    private boolean isMe;
    TagAdapter tagAdapter;
    HomeFeed mHomeFeed;
    @Inject
    AppApi appApi;

    AlbumAdapter adapter;
    List<Album> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        Dialog myDialog = new Dialog(this);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        mHomeFeed = getIntent().getParcelableExtra("parcel_service");
        isMe = getIntent().getExtras().getBoolean("isme");

        if (bRecreate && staticHomeFeed != null) {
            mHomeFeed = staticHomeFeed;
        }

        bRecreate = false;

        if (isMe && mHomeFeed.getUser_id() == CurrentUser.getInstance().getmCustomer().getId()) {
            Edit_btn.setVisibility(View.VISIBLE);
            btn_upgrade.setVisibility(View.VISIBLE);
            findViewById(R.id.container_distance).setVisibility(View.GONE);
            findViewById(R.id.view_walk).setVisibility(View.GONE);
        } else {
            b_gagner.setVisibility(View.GONE);
            Edit_btn.setVisibility(View.GONE);
            btn_upgrade.setVisibility(View.GONE);
            findViewById(R.id.container_distance).setVisibility(View.VISIBLE);
            findViewById(R.id.view_walk).setVisibility(View.VISIBLE);

        }

        btn_upgrade.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("frgToLoad", true);

            startActivity(i);
        });

        if (mHomeFeed.getTherole() == 33) {

            lst_albums.setVisibility(View.GONE);
            findViewById(R.id.txtAddOtherPhotos).setVisibility(View.GONE);

        }

        RelativeLayout custom_statusBar = findViewById(R.id.containerToolbar);
        ImageView plan_img = findViewById(R.id.plan_img);

        plan_img.setVisibility(View.VISIBLE);
        switch (mHomeFeed.getPlan()) {
            case 101:
                plan_img.setImageDrawable(getDrawable(R.drawable.bronze));
                background_card.setImageDrawable(getDrawable(R.drawable.bg_bronze));
                break;
            case 102:
                plan_img.setImageDrawable(getDrawable(R.drawable.silver));
                background_card.setImageDrawable(getDrawable(R.drawable.bg_silver));
                break;
            case 103:
                plan_img.setImageDrawable(getDrawable(R.drawable.gold));
                background_card.setImageDrawable(getDrawable(R.drawable.bg_gold));
                break;
            case 104:
                plan_img.setImageDrawable(getDrawable(R.drawable.diamond));
                background_card.setImageDrawable(getDrawable(R.drawable.bg_diamond));
                break;
            default:
                plan_img.setVisibility(View.GONE);
                break;
        }

        if (mHomeFeed.getPause() == 1) {
            setStatusBarColor(R.color.light_red, false);
            custom_statusBar.setBackgroundColor(getResources().getColor(R.color.light_red));
            ((ImageView)findViewById(R.id.b_back)).setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            ((ImageView)findViewById(R.id.b_edit)).setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        }


        CircleImageView img = findViewById(R.id.img_profil);
        OptimTools.getPicasso(mHomeFeed.getPinLink()).into(img);
        TextView txtName = findViewById(R.id.txtName);
        txtName.setText(mHomeFeed.getFullName());

        OptimTools.getPicasso(mHomeFeed.getServiceImageLink(800)).into(img_featured);


        updateGagner();

        String distance = mHomeFeed.getTheDistance(this);
        if (!distance.matches("")){
            String number = distance.substring(0, distance.length() - 2);
            String unity = distance.substring(distance.length() - 2);
            t_city.setText(number);
            txtUnityDistance.setText(unity);
        }

        t_count.setText(mHomeFeed.getScount() + "");

        //img_grid.setAdapter(imgGridAdapter);

        RecyclerView recyclerView = findViewById(R.id.rvTags);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        tagAdapter = new TagAdapter(false, null);
        recyclerView.setAdapter(tagAdapter);
        TextView title = findViewById(R.id.t_title);
        TextView category = findViewById(R.id.t_categorie);
        TextView description = findViewById(R.id.t_description);

       // min.setText(mHomeFeed.getMin_price());
        title.setText(mHomeFeed.getTitle());
        if (mHomeFeed.getTherole() == 33)
            category.setText(getString(R.string.text_afrili));
        else
            category.setText(mHomeFeed.getCategorie() + " > " + mHomeFeed.getSouscategorie());
        description.setText(mHomeFeed.getDescription());
        if (mHomeFeed.getTags() != null) {
            List<String> tags = new ArrayList<String>(Arrays.asList(mHomeFeed.getTags().split(";")));
            tagAdapter.fill(tags);
        } else{
            findViewById(R.id.txtTags).setVisibility(View.GONE);
        }
        RecyclerView recyclerView2 = findViewById(R.id.rvEvaluation);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter_two = new AvisAdapter();
        recyclerView2.setAdapter(adapter_two);

        appApi.getAvis(mHomeFeed.getId()).enqueue(new Callback<List<Avis>>() {
            @Override
            public void onResponse(Call<List<Avis>> call, Response<List<Avis>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() == 0){
                        findViewById(R.id.t_evaluation).setVisibility(View.GONE);
                    }
                    adapter_two.fill(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Avis>> call, Throwable t) {
                Log.d("ERRORAVIS", "onFailure: " + t.getMessage());
            }
        });


        if (mHomeFeed != null) {
            if (mHomeFeed.getUser_id() != CurrentUser.getInstance().getmCustomer().getId()){
                appApi.touchService(mHomeFeed.getId()).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                    }
                });
            }

            appApi.getViewsService(mHomeFeed.getId()).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && !response.body().isError()) {
                        String s = response.body().getMessage();
                        if (s == "0")
                            s = "1";

                        ((TextView) findViewById(R.id.t_views)).setText(response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {

                }
            });
        }
        albums = new ArrayList<>();
        adapter = new AlbumAdapter(this);
        lst_albums.setLayoutManager(new LinearLayoutManager(this));
        lst_albums.setAdapter(adapter);

        appApi.getAlbums(mHomeFeed.getId()).enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {

                    if (response.body().size() == 0){
                        findViewById(R.id.txtAddOtherPhotos).setVisibility(View.GONE);
                    }

                    if (mHomeFeed.getPlan() > 100) {
                        for (Photo pht : response.body()) {

                            pht.setIspreview(true);
                            boolean bfound = false;
                            for (Album al : albums) {
                                if (al.getTitle().equals(pht.getAlbum())) {
                                    al.getPhotos().add(pht);
                                    bfound = true;
                                }
                            }
                            if (!bfound) {
                                Album new_album = new Album();
                                new_album.setIspreview(true);
                                new_album.setTitle(pht.getAlbum());
                                new_album.getPhotos().add(pht);
                                albums.add(new_album);

                            }
                        }
                    } else {
                        Album new_album = new Album();
                        new_album.setIspreview(true);
                        new_album.setTitle("");
                        albums.add(new_album);

                        for (Photo pht : response.body()) {
                            pht.setIspreview(true);
                            new_album.getPhotos().add(pht);
                        }
                    }

                    adapter.fill(albums);
                } else
                    Alert(getString(R.string.text_erreur_chargement));
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Alert(t.getMessage());
            }
        });

        Edit_btn.setOnClickListener(v -> editService());
        boolean isme = mHomeFeed.getUser_id() == CurrentUser.getInstance().getmCustomer().getId();
        if (!isme) {
            findViewById(R.id.b_message).setVisibility(View.VISIBLE);

//            if (mHomeFeed.getSlots() > 0 && !(mHomeFeed.getIsg() == 0 || mHomeFeed.getGagner() == 0))
//                findViewById(R.id.b_share).setVisibility(View.VISIBLE);

            String phone = mHomeFeed.getPhone();

        }

        findViewById(R.id.b_gps).setOnClickListener(view -> {
            if (mHomeFeed.getUser_id() != CurrentUser.getInstance().getmCustomer().getId()){
                show();
                appApi.customerProfile(mHomeFeed.getUser_id()).enqueue(new Callback<Customer>() {
                    @Override
                    public void onResponse(Call<Customer> call, Response<Customer> response) {
                        if (response.isSuccessful())
                            hide();
                            openGPS(response.body());
                    }

                    @Override
                    public void onFailure(Call<Customer> call, Throwable t) {
                        hide();
                    }
                });
            }else {
                findViewById(R.id.b_gps).setVisibility(View.GONE);
            }
        });

        if (mHomeFeed.getOfficial_account() == 0){
            container_official.setVisibility(View.GONE);
        }else{
            container_official.setVisibility(View.VISIBLE);
        }

        if (mHomeFeed.getStatus() == 0){
            img_active_user.setColorFilter(ContextCompat.getColor(this, R.color.red));
            txt_active.setText(R.string.inactive);
        }else{
            img_active_user.setColorFilter(ContextCompat.getColor(this, R.color.greenSeafoam));
            txt_active.setText(R.string.active);
        }


    }

    private void updateGagner() {

        if (mHomeFeed.getIsg() == 0 || mHomeFeed.getGagner() == 0) {
            b_gagner.setBackground(ServiceActivity.this.getResources().getDrawable(R.drawable.round_button_gray));
        } else {
            b_gagner.setBackground(ServiceActivity.this.getResources().getDrawable(R.drawable.round_button_black));
        }
    }

    @OnClick(R.id.b_gagner)
    public void bGagnerClick() {
        show();
        appApi.swapEtat(mHomeFeed.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    mHomeFeed.setIsg(response.body().getMessage().equals("on") ? 1 : 0);
                    updateGagner();
                }
                hide();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hide();
            }
        });
    }

    static boolean bRecreate = false;
    static HomeFeed staticHomeFeed;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 10022) {
            finish();
            return;
        }
        if (requestCode == 10077) {
            show();

            appApi.getOneService(mHomeFeed.getId()).enqueue(new Callback<HomeFeed>() {
                @Override
                public void onResponse(Call<HomeFeed> call, Response<HomeFeed> response) {
                    if (response.isSuccessful()) {

                        bRecreate = true;
                        staticHomeFeed = response.body();
                        recreate();
                    }
                    hide();
                }

                @Override
                public void onFailure(Call<HomeFeed> call, Throwable t) {
                    hide();
                }
            });

        }
    }

    private void editService() {
        Intent myintent = new Intent(this, EditServiceActivity.class);
        myintent.putExtra("isnew", false);
        myintent.putExtra("parcel_service", mHomeFeed);
        startActivity(myintent);
    }

    @OnClick(R.id.b_message)
    public void sendMessage() {
        Intent myintent = new Intent(this, AddDemandServiceActivity.class);
        myintent.putExtra("service", mHomeFeed);
        startActivity(myintent);
    }

    @OnClick(R.id.img_featured)
    public void imgFeatured() {
        OptimTools.previewPhoto(mHomeFeed.getServiceImageLink(16), this);
    }

//    @OnClick(R.id.b_photos)
//    public void editPhotoes() {
//        Intent myintent = new Intent(this, AlbumsAddEditActivity.class);
//        myintent.putExtra("service", mHomeFeed);
//        startActivity(myintent);
//    }

//    @OnClick(R.id.b_share)
//    public void sendShare() {
//        if (mHomeFeed.getIsg() == 0 || mHomeFeed.getGagner() == 0)
//            return;
//        Intent myintent = new Intent(this, PartagerGagnerActivity.class);
//        myintent.putExtra("service", mHomeFeed);
//        startActivity(myintent);
//    }

    @Override
    public void addPhoto(Album album) {

    }

    @Override
    public void renameAlbum(Album album) {

    }

    @OnClick(R.id.b_back)
    public void onClickBack() {
        finish();
    }

    public void openGPS(Customer customer){
        if (Double.valueOf(customer.getLon()) != 0 && Double.valueOf(customer.getLat()) != 0) {
            Double myLatitude = Double.valueOf(customer.getLat());
            Double myLongitude = Double.valueOf(customer.getLon());
            String labelLocation = customer.getFullName();

                String geoUriString = "geo:" + myLatitude + "," + myLongitude + "?q=(" + labelLocation + ")@" + myLatitude + "," + myLongitude;
                Uri geoUri = Uri.parse(geoUriString);
                Intent mapCall = new Intent(Intent.ACTION_VIEW, geoUri);
                startActivity(mapCall);

        }else{
            Toast.makeText(this, "La position n'est pas disponible", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        show();
        appApi.getOneService(mHomeFeed.getId())
                .enqueue(new Callback<HomeFeed>() {
                    @Override
                    public void onResponse(Call<HomeFeed> call, Response<HomeFeed> response) {
                        hide();
                        if (response.isSuccessful()) {
                            mHomeFeed = response.body();
                        }

                    }

                    @Override
                    public void onFailure(Call<HomeFeed> call, Throwable t) {
                        hide();
                        Alert(t.getMessage());

                    }
                });
    }
}

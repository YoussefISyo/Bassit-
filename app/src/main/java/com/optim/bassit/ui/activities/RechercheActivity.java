package com.optim.bassit.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.optim.bassit.App;
import com.optim.bassit.PagedRecyclerView;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.AppData;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Ads;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.models.SousCategorie;
import com.optim.bassit.ui.adapters.CatsAdapter;
import com.optim.bassit.ui.adapters.RechercheResultAdapter;
import com.optim.bassit.ui.adapters.RechercheSousCategorieAdapter;
import com.optim.bassit.utils.OptimTools;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;


public class RechercheActivity extends BaseActivity implements RechercheSousCategorieAdapter.ItemClickListener, RechercheResultAdapter.ItemClickListener, CatsAdapter.ItemClickListener {

    RechercheSousCategorieAdapter adapterSousCategories;
    RechercheResultAdapter adapterresult;
    CatsAdapter adapterCats;
    Dialog myDialog;
    public static List<Ads> listSearchAds;

    @BindView(R.id.search_view)
    EditText editText;

    @Inject
    AppApi appApi;
    SwipeRefreshLayout swipe;
    private PagedRecyclerView paged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);
        swipe = findViewById(R.id.swipe);

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == IME_ACTION_SEARCH) {
                getSearchHome(1);
                return true;
            }
            return false;
        });

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

        RecyclerView recyclerViewVr = findViewById(R.id.lst_result);
        recyclerViewVr.setLayoutManager(new LinearLayoutManager(this));
        adapterresult = new RechercheResultAdapter(this);
        recyclerViewVr.setAdapter(adapterresult);

        paged = OptimTools.injectPaginationHavingLayoutManager(recyclerViewVr, (page, total) -> getSearchHome(page));
        myDialog = new Dialog(this);

        swipe.setOnRefreshListener(this::pullRefresh);


        Intent intent = getIntent();
        int cat = intent.getIntExtra("CAT", -1);

        onSelectCategory(cat);
    }

    private void pullRefresh() {
        adapterCats.toggleSelected(-1);
        updateSousCategories();
        editText.setText("");
        getSearchHome(1);
        paged.resetState();
    }


    @Override
    public void onItemClick(Categorie cat) {
        onSelectCategory(cat.getId());
    }

    private void onSelectCategory(int cat) {
        adapterCats.toggleSelected(cat);
        updateSousCategories();
        getSearchHome(1);
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

    @Override
    public void onItemClick(SousCategorie kind) {
        adapterSousCategories.toggleSelected(kind.getId());
        getSearchHome(1);


    }

    @Override
    public void onItemClick(HomeFeed homefeed) {
        Intent myintent = new Intent(this, ServiceActivity.class);
        boolean isme = false;
        if (CurrentUser.getInstance().isLogin() && CurrentUser.getInstance().getmCustomer().getId() == homefeed.getUser_id())
            isme = true;

        myintent.putExtra("isme", isme);
        myintent.putExtra("parcel_service", homefeed);
        startActivity(myintent);
    }

    private void getSearchHome(Integer page) {

        show();
        appApi.getSearchHomeCats(adapterSousCategories.selectedSousCategorie(), adapterCats.selectedCat(), editText.getText().toString(), page).enqueue(new Callback<List<HomeFeed>>() {
            @Override
            public void onResponse(Call<List<HomeFeed>> call, Response<List<HomeFeed>> response) {
                if (response.isSuccessful())
                    adapterresult.fill(page, response.body());
                swipe.setRefreshing(false);
                hide();
            }

            @Override
            public void onFailure(Call<List<HomeFeed>> call, Throwable t) {
                swipe.setRefreshing(false);
                hide();
            }
        });
    }

    @OnClick(R.id.btn_back)
    public void goBack() {
        finish();
    }

    @Override
    public void onAdsClick(HomeFeed homefeed) {
        try {
            switch (homefeed.getIsg()) {
                case 0:


                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(homefeed.getTitle()));
                    startActivity(browserIntent);
                    break;
                case 1:
                    Integer u_id = Integer.valueOf(homefeed.getTitle());
                    Intent myintent = new Intent(this, ProfileActivity.class);
                    myintent.putExtra("user_id", u_id);
                    startActivity(myintent);
                    break;
                case 2:
                    Integer s_id = Integer.valueOf(homefeed.getTitle());
                    appApi.getOneService(s_id).enqueue(new Callback<HomeFeed>() {
                        @Override
                        public void onResponse(Call<HomeFeed> call, Response<HomeFeed> response) {
                            if (response.isSuccessful()) {
                                Intent myintent = new Intent(RechercheActivity.this, ServiceActivity.class);
                                myintent.putExtra("isme", false);
                                myintent.putExtra("parcel_service", response.body());
                                startActivity(myintent);
                            }
                        }

                        @Override
                        public void onFailure(Call<HomeFeed> call, Throwable t) {

                        }
                    });

                    break;
            }
        } catch (Exception ex) {

        }

    }
}

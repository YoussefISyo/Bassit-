package com.optim.bassit.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.base.BaseFragment;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.di.modules.NetworkModule;
import com.optim.bassit.models.Customer;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.ui.activities.EditInformationActivity;
import com.optim.bassit.ui.activities.FinanceActivity;
import com.optim.bassit.ui.activities.MainActivity;
import com.optim.bassit.ui.activities.ProfileActivity;
import com.optim.bassit.ui.activities.ServiceActivity;
import com.optim.bassit.ui.activities.ServiceAddEditActivity;
import com.optim.bassit.ui.activities.StatistiquesActivity;
import com.optim.bassit.ui.activities.UpdateMyProfileActivity;
import com.optim.bassit.ui.adapters.AvisAdapter;
import com.optim.bassit.ui.adapters.FlexboxLayoutManagerBugless;
import com.optim.bassit.ui.adapters.ProfileAdapter;
import com.optim.bassit.ui.adapters.TagAdapter;
import com.optim.bassit.utils.OptimTools;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import static com.optim.bassit.ui.activities.MainActivity.linearLayout;

public class MyProfileFragment extends BaseFragment implements ProfileAdapter.ItemClickListener {

    ProfileAdapter adapter;
    AvisAdapter adapter_two;
    SwipeRefreshLayout swipeView;
    @BindView(R.id.profile_img)
    CircleImageView mImg;
    @BindView(R.id.b_change_profile_img)
    ImageView bEditAvatar;
    @Inject
    AppApi appApi;
    private int user_id;
    @BindView(R.id.b_back)
    ImageButton Back_btn;
    @BindView(R.id.b_delete)
    LinearLayout bDelete;
    @BindView(R.id.b_add_service)
    FloatingActionButton fab;

    @BindView(R.id.b_edit)
    LinearLayout bEdit;
    @BindView(R.id.mToolbarProfile)
    Toolbar mToolbarProfile;


    public MyProfileFragment() {
        // Required empty public constructor
    }

    public static MyProfileFragment newInstance(int user_id) {
        MyProfileFragment fragment = new MyProfileFragment();

        Bundle args = new Bundle();
        args.putInt("user_id", user_id);
        fragment.setArguments(args);
        return fragment;
    }
   // LinearLayout linearLayout;
    Handler mHandler=new Handler();
    public  void timer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(8000);
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            linearLayout.setVisibility(View.GONE);
                            //runb=false;
                        }
                    });

                } catch(Exception e){
                }
            }

        }).start();
    }
    public static MyProfileFragment newInstance() {
        MyProfileFragment fragment = new MyProfileFragment();
        return fragment;
    }
    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }
    public  Date strtodate(String date){
        Date date1= null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }
    View mvw;
    CardView linearLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_myprofile, container, false);
        mvw = vw;
        ButterKnife.bind(this, vw);

        setHasOptionsMenu(true);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbarProfile);

        if (bMain) {
            Back_btn.setVisibility(View.GONE);
        } else {
            bEditAvatar.setVisibility(View.GONE);
            bEdit.setVisibility(View.GONE);
        }

        if (CurrentUser.getInstance().getmCustomer().getPause() == 1 && bMain) {
            ((BaseActivity) getActivity()).setStatusBarColor(R.color.light_red, false);
            ((ImageView) vw.findViewById(R.id.top_image)).setImageDrawable(getResources().getDrawable(R.drawable.profile_bg_pause));
        } else {
            ((BaseActivity) getActivity()).setStatusBarColor(R.color.colorPrimary, false);
            ((ImageView) vw.findViewById(R.id.top_image)).setImageDrawable(getResources().getDrawable(R.drawable.profile_bg));
        }
        try {
            if (CurrentUser.getInstance()!=null && CurrentUser.getInstance().isPro()){
              linearLayout=(CardView)  mvw.findViewById(R.id.remb);
                linearLayout.setVisibility(View.GONE);
                if(CurrentUser.getInstance().getmCustomer().getPhone()==null || CurrentUser.getInstance().getmCustomer().getPhone().isEmpty())
                {
                    String date="2020-05-22 23:21:29.766";
                    SharedPreferences prefs = getContext().getSharedPreferences("forgetP",0);
                    date = prefs.getString("dates","2020-05-22 22:21:29.766");
                    long diff = new Date().getTime() - strtodate(date).getTime();

                    int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
                    //  System.out.println("difference between days: " + diffDays);

                    if(diffDays>0){
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("dates", getCurrentTimeStamp());
                        editor.apply();
                    linearLayout.setVisibility(View.VISIBLE);
                    timer();}}
            }}catch (Exception e){}
        mvw.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_profile = new Intent(getActivity(), EditInformationActivity.class);
                startActivity(intent_profile);
            }
        });
      //


        int user_id = getArguments() != null ? getArguments().getInt("user_id", 0) : 0;

        RecyclerView recyclerView = vw.findViewById(R.id.rvServices);
        recyclerView.setLayoutManager(new LinearLayoutManager(vw.getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new ProfileAdapter(this);
        recyclerView.setAdapter(adapter);
        this.user_id = user_id;
        if (user_id == 0)
            FillUserData(vw, CurrentUser.getInstance().getmCustomer());
        else {
            appApi.customerProfile(user_id).enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    if (response.isSuccessful())
                        FillUserData(vw, response.body());
                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {

                }
            });
        }
        if (user_id != 0)
            Back_btn.setOnClickListener(v -> getActivity().finish());

        return vw;
    }


    String phone;

    private void FillUserData(View vw, Customer customer) {
        Customer mCustomer = customer;
        TextView username = vw.findViewById(R.id.t_username);

        TextView t_services = vw.findViewById(R.id.t_services);

        LinearLayout txt_official_account = vw.findViewById(R.id.txt_official_account);
        if (customer.getOfficial_account() == 1){
            txt_official_account.setVisibility(View.VISIBLE);
        }else{
            txt_official_account.setVisibility(View.GONE);
        }

        TextView description = vw.findViewById(R.id.t_description);
        TextView txt_points = vw.findViewById(R.id.txt_points);
        TextView txt_wallet = vw.findViewById(R.id.txt_wallet);
        LinearLayout services = vw.findViewById(R.id.service_layout);
        LinearLayout container_points = vw.findViewById(R.id.container_points);

        username.setText(mCustomer.getFullName());
        description.setText(mCustomer.getDescription());

        ImageView img = vw.findViewById(R.id.profile_img);
        OptimTools.getPicasso(mCustomer.getPinLink()).into(img);

        ImageView plan_img = vw.findViewById(R.id.plan_img);

        RecyclerView recyclerView = vw.findViewById(R.id.rvTags);
        FlexboxLayoutManagerBugless layoutManager = new FlexboxLayoutManagerBugless(vw.getContext());

        recyclerView.setLayoutManager(layoutManager);
        TagAdapter tagAdapter = new TagAdapter(false, null);
        recyclerView.setAdapter(tagAdapter);


        if (customer.getTags() != null) {
            List<String> tags = new ArrayList<>(Arrays.asList(customer.getTags().split(";")));
            tagAdapter.fill(tags);
        }

        int plan = 0;
        boolean isme = customer.getId() == CurrentUser.getInstance().getmCustomer().getId();
        if (!isme) {
            setHasOptionsMenu(false);
            container_points.setVisibility(View.GONE);
            phone = customer.getPhone();
            plan = customer.getPlan();
            if (user_id != 0) {

                if (phone != null && !phone.matches("")) {
                    //vw.findViewById(R.id.b_call).setVisibility(View.VISIBLE);
                    vw.findViewById(R.id.b_call).setOnClickListener(view -> {
                        Uri u = Uri.parse("tel:" + phone);
                        Intent intent = new Intent(Intent.ACTION_DIAL, u);
                        getActivity().startActivity(intent);
                    });
                }
                if (Double.valueOf(customer.getLon()) != 0 && Double.valueOf(customer.getLat()) != 0) {
                    Double myLatitude = Double.valueOf(customer.getLat());
                    Double myLongitude = Double.valueOf(customer.getLon());
                    String labelLocation = customer.getFullName();
                    vw.findViewById(R.id.b_gps).setVisibility(View.VISIBLE);
                    vw.findViewById(R.id.b_gps).setOnClickListener(view -> {
                        String geoUriString = "geo:" + myLatitude + "," + myLongitude + "?q=(" + labelLocation + ")@" + myLatitude + "," + myLongitude;
                        Uri geoUri = Uri.parse(geoUriString);
                        Intent mapCall = new Intent(Intent.ACTION_VIEW, geoUri);
                        startActivity(mapCall);
                    });
                }
            }
        } else {
            setHasOptionsMenu(true);
            plan = CurrentUser.getInstance().getmCustomer().getPlan();
            if (CurrentUser.getInstance().isPro()){
                container_points.setVisibility(View.VISIBLE);
            }
            txt_points.setText(CurrentUser.getInstance().getmCustomer().getFullPoints(this.getActivity()));
            txt_wallet.setText(new DecimalFormat("#").format(CurrentUser.getInstance().getmCustomer().getCredit()) + " " + getString(R.string.text_da));
        }
        plan_img.setVisibility(View.VISIBLE);
        switch (plan) {
            case 101:
                plan_img.setImageDrawable(getActivity().getDrawable(R.drawable.bronze));
                break;
            case 102:
                plan_img.setImageDrawable(getActivity().getDrawable(R.drawable.silver));
                break;
            case 103:
                plan_img.setImageDrawable(getActivity().getDrawable(R.drawable.gold));
                break;
            case 104:
                plan_img.setImageDrawable(getActivity().getDrawable(R.drawable.diamond));
                break;
            default:
                plan_img.setVisibility(View.GONE);
                break;
        }

        if (mCustomer.getTherole() == 2 || mCustomer.getTherole() == 33) {
            description.setText(mCustomer.getDescription());

            description.setVisibility(View.VISIBLE);
            services.setVisibility(View.VISIBLE);
            if (mCustomer.getTherole() == 33)
                fab.hide();
            else if (user_id == 0)
                fab.show();
            else
                fab.hide();
            swipeView = vw.findViewById(R.id.swipe_layout);
            swipeView.setOnRefreshListener(() -> getServices());

            t_services.setText(getString(R.string.text_service_propose) + " " + mCustomer.getFullName());
            getServices();


        } else {
            description.setVisibility(View.GONE);
            services.setVisibility(View.GONE);
            fab.hide();
        }

    }

    @OnClick(R.id.b_edit)
    public void editProfile() {
        Intent myintent = new Intent(this.getContext(), EditInformationActivity.class);
        //startActivityForResult(myintent, 10007);
    }


    @OnClick(R.id.profile_img_layout)
    public void updateAvatar() {
        if (!bMain)
            return;

        PickImageDialog.build(new PickSetup()
                .setTitle(getActivity().getString(R.string.text_choose_avatar))
                .setTitleColor(R.color.colorPrimaryDark)
                .setGalleryButtonText(getString(R.string.galerie))
                .setCameraButtonText(getString(R.string.camera))
                .setCancelText(getActivity().getString(R.string.text_cancel))
                .setPickTypes(EPickType.GALLERY, EPickType.CAMERA)
                .setButtonOrientation(LinearLayout.HORIZONTAL)
                .setSystemDialog(false))
                .setOnPickResult(r -> {
                    if (r.getError() == null) {

                        show(mvw);


                        File file1 = new File(r.getPath());
                        File file = OptimTools.resizeFile(file1, 500, getContext());

                        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);

                        BaseActivity ba = ((BaseActivity) getActivity());
                        appApi.uploadImage(part).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                hide(mvw);
                                if (!response.isSuccessful())
                                    ba.Alert(R.string.error_server);
                                else if (response.body().isError())
                                    ba.Alert(response.body().getMessage());
                                else {
                                    OptimTools.getPicasso(NetworkModule.PIN_URL + response.body().getMessage()).into(mImg);
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Alert(t.getMessage());
                                hide(mvw);
                            }
                        });


                    } else {
                        //Handle possible errors
                        //TODO: do what you have to do with r.getError();
                        Toast.makeText(getContext(), r.getError().getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .setOnPickCancel(() -> {
                    //TODO: do what you have to if user clicked cancel
                }).show(getActivity().getSupportFragmentManager());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
        try {
            MainActivity ma = (MainActivity) getActivity();
            bMain = true;
        } catch (Exception ex) {

        }


    }

    boolean bMain = false;

    private void getServices() {
        appApi.getServicesByUser(user_id == 0 ? CurrentUser.getInstance().getmCustomer().getId() : user_id).enqueue(new Callback<List<HomeFeed>>() {
            @Override
            public void onResponse(Call<List<HomeFeed>> call, Response<List<HomeFeed>> response) {
                if (response.isSuccessful()) {
                    if (bMain && response.body().size() == 0) {

                        if (CurrentUser.getInstance().getmCustomer().getTherole() == 33)
                            getView().findViewById(R.id.b_add_service).setVisibility(View.VISIBLE);

                        bDelete.setVisibility(View.VISIBLE);
                    }


                    adapter.fill(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<HomeFeed>> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.b_delete)
    public void onDeletePro() {

        BaseActivity act = (BaseActivity) getActivity();

        act.AlertYesNo(R.string.message_cancel_bassit_pro, () -> {
            show(mvw);
            appApi.undoPro().enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    hide(mvw);
                    if (!response.isSuccessful())
                        Alert(R.string.error_server);
                    else if (response.body().isError())
                        Alert(MyProfileFragment.this.getString(R.string.message_cancelation_error));
                    else {

                        Intent i = getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        getActivity().finish();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Alert(R.string.error_server);
                    hide(mvw);
                }
            });
        });

    }

    @OnClick(R.id.b_add_service)
    public void onAddService() {
        if (Double.valueOf(CurrentUser.getInstance().getmCustomer().getLat()) == 0 && Double.valueOf(CurrentUser.getInstance().getmCustomer().getLon()) == 0) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.message_personal_information_required)
                    .setPositiveButton(R.string.text_ok, (d, i) -> {
                        d.dismiss();
                        Intent myintent = new Intent(this.getContext(), UpdateMyProfileActivity.class);
                        startActivityForResult(myintent, 10008);
                    }).show();


        } else {
            Intent myintent = new Intent(this.getContext(), ServiceAddEditActivity.class);
            myintent.putExtra("isnew", true);
            startActivityForResult(myintent, 10007);
        }
    }

    @Override
    public void onItemClick(HomeFeed homeFeed) {
        Intent myintent = new Intent(this.getContext(), ServiceActivity.class);
        myintent.putExtra("isme", true);
        myintent.putExtra("parcel_service", homeFeed);
        startActivityForResult(myintent, 10007);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 10007)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getFragmentManager().beginTransaction().detach(this).commitNow();
                getFragmentManager().beginTransaction().attach(this).commitNow();
            } else {
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            }
        } else if (resultCode == 10008) {
            Intent myintent = new Intent(this.getContext(), ServiceAddEditActivity.class);
            myintent.putExtra("isnew", true);
            startActivityForResult(myintent, 10007);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        if (!CurrentUser.getInstance().isPro()){
            MenuItem financeItem = menu.findItem(R.id.finance_item);
            MenuItem statItem = menu.findItem(R.id.stat_item);

            financeItem.setVisible(false);
            statItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.info_item:
                Intent intent_profile = new Intent(getActivity(), EditInformationActivity.class);
                startActivity(intent_profile);
                return true;
            case R.id.finance_item:
                Intent intent_finance = new Intent(getActivity(), FinanceActivity.class);
                startActivity(intent_finance);
                return true;
            case R.id.stat_item:
                Intent intent_stats = new Intent(getActivity(), StatistiquesActivity.class);
                startActivity(intent_stats);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


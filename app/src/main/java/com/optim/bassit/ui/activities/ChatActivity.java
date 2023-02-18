package com.optim.bassit.ui.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optim.bassit.App;
import com.optim.bassit.MyFirebaseNotification;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Chat;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.models.IconizedMenu;
import com.optim.bassit.models.Tache;
import com.optim.bassit.ui.adapters.ConversationAdapter;
import com.optim.bassit.ui.dialogs.CloturerDialogFragment;
import com.optim.bassit.ui.dialogs.EvaluationDialogFragment;
import com.optim.bassit.ui.fragments.RappelFragment;
import com.optim.bassit.utils.MapHelper;
import com.optim.bassit.utils.OptimTools;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.optim.bassit.ui.activities.MainActivity.bottomNav2;
import static com.optim.bassit.ui.activities.MainActivity.context;
import static com.optim.bassit.ui.activities.MainActivity.removeBadge;
import static com.optim.bassit.ui.activities.MainActivity.showBadge;

public class ChatActivity extends BaseActivity implements RappelFragment.ItemClickListener, ConversationAdapter.ItemClickListener, MyFirebaseNotification.NotificationListener {

    Dialog myDialog;
    @BindView(R.id.b_chat_back)
    ImageButton Back;
    @BindView(R.id.t_user)
    TextView t_user;
    @BindView(R.id.t_etat)
    TextView t_etat;

   @BindView(R.id.input)
    LinearLayout input;
    @BindView(R.id.b_chat_menu)
    ImageButton Menu;
    int service_id;
    String offer_id="";
    String typec="";
   HomeFeed mHomeFeed;

    int meId;
    int himId;

    @BindView(R.id.t_message)
   EditText t_message;
    @BindView(R.id.lst)
    RecyclerView lst;
    //PopupMenu popup;
    IconizedMenu popup;
    @Inject
    AppApi appApi;
    private ConversationAdapter adapter;
    int iclient;

    Tache mTache;

    @BindView(R.id.chat_layout)
    RelativeLayout chatLayout;
    String bonid="-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        MyFirebaseNotification.registerNotification(this);
        App.getApp(getApplication()).getComponent().inject(this);
        myDialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

        if (getIntent().hasExtra("notification")) {
          /*  if(appApi==null)
            {
                Intent intent=new Intent(ChatActivity.this,SplashActivity.class);
                intent.putExtra("notification",getIntent().getStringExtra("notification"));
                startActivity(intent);
                finish();

            }*/

          appApi.getOneTaches(getIntent().getStringExtra("notification")).enqueue(new Callback<Tache>() {
                @Override
                public void onResponse(Call<Tache> call, Response<Tache> response) {

                    if (response.isSuccessful()) {
                        mTache = response.body();
                        bonid=mTache.getBon_id()+"";
                        if(mTache.getTypec().equals("offer"))
                        {
                            offer_id=mTache.getOffer_id();
                            typec="offer";

                        }
                        handleIfTache();

                        afterLoadingData();
                    } else {
                        AlertWait(R.string.chat_nexiste_plus, () -> {
                            finish();
                        });
                    }
                }

                @Override
                public void onFailure(Call<Tache> call, Throwable t) {
                    AlertWait(R.string.chat_nexiste_plus, () -> {
                        finish();
                    });
                }
            });

            return;

        } else if (getIntent().hasExtra("service")) {
            mHomeFeed = getIntent().getParcelableExtra("service");
            himId = getIntent().getIntExtra("him_id", 0);
            meId = CurrentUser.getInstance().getmCustomer().getId();
            service_id = mHomeFeed.getId();
            iclient = 1;
            if (getIntent().hasExtra("offer_id")){
                offer_id=getIntent().getStringExtra("offer_id");
                typec="offer";
            }

        } else if (getIntent().hasExtra("tache")) {

            mTache = getIntent().getParcelableExtra("tache");
            bonid=mTache.getBon_id()+"";
            if (getIntent().hasExtra("offer_id")){
                offer_id=getIntent().getStringExtra("offer_id");
                typec=getIntent().getStringExtra("typec");
            }
          /*  if(mTache.getTypec().equals("offer"))
            {
                offer_id=mTache.getOffer_id();
                typec="offer";

            }*/
            handleIfTache();
            if (getIntent().hasExtra("action")){
                action=getIntent().getStringExtra("action");

            }

        }


        afterLoadingData();
    }
    String action="";
    void  opendialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setTitle(getString(R.string.choose_st));
// add a radio button list
        String[] animals = {getString(R.string.fini_suc),getString(R.string.fini_witout_suc)};
        int checkedItem = 0; // cow
        final int[] checked = {0};
        builder.setSingleChoiceItems(animals, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //    Toast.makeText(myofferActivity.this,which+"",Toast.LENGTH_LONG).show();
                checked[0] =which;
            }
        });
// add OK and Cancel buttons
        builder.setPositiveButton(getString(R.string.text_ok), new DialogInterface.OnClickListener() {



            @Override
            public void onClick(DialogInterface dialog, int which) {
                //    Toast.makeText(myofferActivity.this, checked[0] +"",Toast.LENGTH_LONG).show();
                if(checked[0]==0) {
                  b_success();
                }else {
                    // user clicked OK
                    b_fail();


                }
            }
        });
        builder.setNegativeButton(getString(R.string.text_cancel), null);
// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();


    }
    AlertDialog lc;
    AlertDialog  al;
    private void savehim(String him,String see) {

        Map<String, String> dd=gethim();
        dd.put(him,see);
        String json=  new Gson().toJson(dd);
        SharedPreferences settings = getSharedPreferences("compte", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("him",json);
        editor.apply();
    }
    public Map<String, String> gethim() {
        SharedPreferences settings = getSharedPreferences("compte", 0);
        Map<String, String> data=new HashMap<String, String>();
        if(!settings.getString("him","").equals(""))
            data= revert(settings.getString("him",""));
        return data;
    }
    public static Map<String, String> revert(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> map = gson.fromJson(json, type);
        return map;
    }
    public void setnumchat(String num) {
        SharedPreferences settings = getSharedPreferences("compte", 0);
        SharedPreferences.Editor editor = settings.edit();
        int s=0;
        if(!getnumchat().equals("0")) s=Integer.valueOf(getnumchat())-Integer.valueOf(num);
        editor.putString("numchat",s+"");
        editor.apply();
    }
    public String getnumchat() {
        SharedPreferences settings = getSharedPreferences("compte", 0);
        return settings.getString("numchat","0");
    }
    private void handleIfTache() {
        meId = CurrentUser.getInstance().getmCustomer().getId();
        service_id = mTache.getService_id();
        if (mTache.getStatus() == 2)
            t_etat.setText(R.string.cloture_success);
        else if (mTache.getStatus() == 99)
            t_etat.setText(R.string.cloture_echouee);
        else t_etat.setText(R.string.non_finie);


        if(mTache.getStatus() == 2 || mTache.getStatus() == 99){
            input.setVisibility(View.GONE);
        }

        else {
            input.setVisibility(View.VISIBLE);
        }


        if (CurrentUser.getInstance().getmCustomer().getId() == mTache.getClient_id())
            iclient = 1;
        else
            iclient = 0;
    }

    private void afterLoadingData() {

        if (mTache != null && mTache.getStatus() != 0) {
            if (mTache.getClient_avis() == null && iclient == 1 && !typec.equals("offer")) {
                FragmentManager fm = getSupportFragmentManager();
                EvaluationDialogFragment editNameDialogFragment = EvaluationDialogFragment.newInstance(mTache.getBon_id());
                editNameDialogFragment.show(fm, "fragment_cloturer");
            }
        }
        if (himId == 0)
            if (mHomeFeed == null) {
                if (CurrentUser.getInstance().getmCustomer().getId() == mTache.getUser_service_id())
                    himId = mTache.getClient_id();
                else
                    himId = mTache.getUser_service_id();

            } else
                himId = mHomeFeed.getUser_id();
savehim(himId+"","seen");
setnumchat("1");
        if(!getnumchat().equals("0")) {
            try {
                showBadge(context, bottomNav2,2,getnumchat());
            }catch (Exception e){}

            ShortcutBadger.applyCount(getApplicationContext(), Integer.parseInt(getnumchat()));
        }else{
            try {
                removeBadge(bottomNav2, 2);
            }catch (Exception e){}
            ShortcutBadger.removeCount(getApplicationContext());}
        ImageButton btn_menu = findViewById(R.id.b_chat_menu);
        Context myContext = new ContextThemeWrapper(ChatActivity.this,R.style.PopupTheme);
        popup = new IconizedMenu(myContext, btn_menu);


        if (iclient == 0 && mTache != null && mTache.getStatus() == 0){

            if(typec.equals("offer")){
                popup.getMenuInflater().inflate(R.menu.chat_menu_popup2, popup.getMenu());
                popup.getMenu().findItem(R.id.b_transfer).setVisible(false);
                popup.getMenu().findItem(R.id.b_block).setVisible(false);
            }else {
                popup.getMenuInflater().inflate(R.menu.chat_menu_popup, popup.getMenu());
                if(action.equals("close"))
                opendialog();
            }
        }
        else{

            if(typec.equals("offer")){
                popup.getMenuInflater().inflate(R.menu.chat_menu_popup, popup.getMenu());
                if(mTache != null && mTache.getStatus()!=0)  popup.getMenu().findItem(R.id.b_cloturer).setVisible(false);
                popup.getMenu().findItem(R.id.b_transfer).setVisible(false);
                popup.getMenu().findItem(R.id.b_block).setVisible(false);
                if(action.equals("close") && mTache != null && mTache.getStatus() == 0)
                opendialog();
            }else popup.getMenuInflater().inflate(R.menu.chat_menu_popup2, popup.getMenu());
        }

        for(int i = 0; i < popup.getMenu().size(); i++){
            Drawable drawable = popup.getMenu().getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            }
        }

        if (mTache != null && mTache.getStatus() == 0) {

            if (mTache.getLink_id() > 0)
                popup.getMenu().findItem(R.id.b_transfer).setVisible(false);
            appApi.blocage(himId).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getMessage().equals("0;0"))
                            input.setVisibility(View.VISIBLE);
                        else if (response.body().getMessage().equals("1;0") || response.body().getMessage().equals("1;1")) {
                            popup.getMenu().findItem(R.id.b_block).setTitle(R.string.text_autoriser);
                            input.setVisibility(View.GONE);
                        } else if (response.body().getMessage().equals("0;1")) {
                            popup.getMenu().findItem(R.id.b_block).setTitle(R.string.text_bloque);
                            input.setVisibility(View.GONE);
                        }
                    } else
                        Alert(response.errorBody().toString());
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Alert(t.getMessage());
                }
            });

        } else {
            popup.getMenu().findItem(R.id.b_block).setVisible(false);
        }
        TextView username = findViewById(R.id.t_user);
        CircleImageView avatar = findViewById(R.id.img_avatar);

        String avatarlink;
        if (mHomeFeed == null) {
            username.setText(iclient == 0 ? mTache.getFullClientBName() : mTache.getFullServiceBName());
            avatarlink = iclient == 0 ? mTache.getClientPinLink() : mTache.getServicePinLink();
            OptimTools.getPicasso(avatarlink).into(avatar);
        } else {
            username.setText(mHomeFeed.getFullName());
            avatarlink = mHomeFeed.getPinLink();
            OptimTools.getPicasso(mHomeFeed.getPinLink()).into(avatar);
        }

        LinearLayout bottom_sheet = findViewById(R.id.bottom_sheet);
        BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        adapter = new ConversationAdapter(this, iclient, avatarlink, sheetBehavior);

        lst.setLayoutManager(new LinearLayoutManager(this));
        lst.setAdapter(adapter);


        getConversation();


        Back.setOnClickListener(v -> finish());


        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.b_fail)
    public void b_fail() {
        int id=0;
        try {
            id=Integer.valueOf(bonid);
        }catch (Exception e){

        }
        if(mTache != null && mTache.getBon_id() > 0)
            id=mTache.getBon_id();
        appApi.failTache(id).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    if (response.body().isError()) {
                        Alert(response.body().getMessage());
                    } else {
                        new AlertDialog.Builder(ChatActivity.this)
                                .setTitle(R.string.app_name)
                                .setMessage(R.string.text_tache_cloture)
                                .setPositiveButton(R.string.text_ok, (dialogInterface, i) -> {
                                    setResult(10077);
                                    finish();

                                }).show();

                    }
                } else {
                    Alert(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Alert(t.getMessage());
            }
        });
    }

    @OnClick(R.id.b_success)
    public void b_success() {
        int id=0;
        try {
            id=Integer.valueOf(bonid);
        }catch (Exception e){

        }
        if(mTache != null && mTache.getBon_id() > 0)
            id=mTache.getBon_id();
        if(typec.equals("offer")){
            FragmentManager fm = getSupportFragmentManager();
            EvaluationDialogFragment editNameDialogFragment = EvaluationDialogFragment.newInstance(id, "offer", offer_id + "", "");
            editNameDialogFragment.show(fm, "fragment_cloturer");
        }else{
            FragmentManager fm = getSupportFragmentManager();
            CloturerDialogFragment editNameDialogFragment = CloturerDialogFragment.newInstance(id,"","","");
            editNameDialogFragment.show(fm, "fragment_cloturer");
        }


    }

    @OnClick(R.id.b_top)
    public void b_top() {
        Intent myintent = new Intent(this, ProfileActivity.class);
        myintent.putExtra("user_id", himId);
        startActivity(myintent);
    }

   private void getConversation() {

        if (iclient == 1 && mTache == null)
            appApi.getChat(service_id,typec,offer_id).enqueue(new Callback<List<Chat>>() {
                @Override
                public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                    if (!response.isSuccessful()) {

                        Alert(R.string.error_server);
                        return;
                    }
                    if(response.body()!=null && response.body().size()>0)
                        bonid= response.body().get(0).getBon_id()+"";

                 //   Toast.makeText(ChatActivity.this,response.body().get(0).getBon_id()+" , "+bonid,Toast.LENGTH_LONG).show();
                    adapter.fill(response.body());
                    fixScroll();
                }

                @Override
                public void onFailure(Call<List<Chat>> call, Throwable t) {
                    Alert(R.string.error_server);
                }
            });
        else
            appApi.getChatByBon(mTache.getBon_id()).enqueue(new Callback<List<Chat>>() {
                @Override
                public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                    if (!response.isSuccessful()) {
                        Alert(R.string.error_server);
                        return;
                    }
                    adapter.fill(response.body());
                    fixScroll();
                }

                @Override
                public void onFailure(Call<List<Chat>> call, Throwable t) {
                    Alert(R.string.error_server);
                }
            });
    }


    private void sendMessage(Chat mChat) {
        String ss = OptimTools.dateTimeToFRString(new Date());
        mChat.setChk(ss);
        adapter.addOne(mChat);
        t_message.setText("");
        fixScroll();
        MultipartBody.Part part = null;
        if (mChat.getType() == 1) {
            File file1 = new File(mChat.getMessage());
            File file = OptimTools.resizeFile(file1, 1000,this);

            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);


        } else {
            part = MultipartBody.Part.createFormData("image", "");
        }
        appApi.sendChat(mChat.getService_id(),typec,offer_id, mChat.getType(), mChat.getMessage(), mChat.getBon_id(), part).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!response.isSuccessful()) {
                    Alert(R.string.error_server);
                    adapter.removeByTemp(ss);
                    t_message.setText(mChat.getMessage());

                } else if (response.body().isError()) {
                    Alert(response.body().getMessage());
                    adapter.removeByTemp(ss);
                    t_message.setText(mChat.getMessage());
                }else {
                    if(response.body().getId()!=null)
                        bonid=response.body().getId();
                    //Toast.makeText(ChatActivity.this,response.body().getId()+" , "+bonid,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Alert(R.string.error_server);
                adapter.removeByTemp(ss);
                t_message.setText(mChat.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        MyFirebaseNotification.unregisterNotification(this);
        super.onDestroy();
    }

    @OnClick(R.id.b_chat_menu)
    public void B_MenuClick() {
        int id=0;
        try {
            id=Integer.valueOf(bonid);
        }catch (Exception e){

        }
        if(mTache != null && mTache.getBon_id() > 0)
            id=mTache.getBon_id();

       // Toast.makeText(ChatActivity.this,bonid,Toast.LENGTH_LONG).show();
          int  finalId = id;
        popup.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent();
            switch (item.getItemId()) {
                case R.id.b_cloturer:
                  //  if (mTache != null && mTache.getBon_id() > 0 ) {
                    if (finalId > 0 ) {
                        LinearLayout bottom_sheet = findViewById(R.id.bottom_sheet);
                        BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    } else {
                        Alert(R.string.text_veuillez_ressayer);
                    }
                    break;
                case R.id.b_transfer:
                    intent = new Intent(myDialog.getContext(), TransfererActivity.class);
                    intent.putExtra("bon_id", mTache.getBon_id());
                    startActivityForResult(intent, 10033);
                    break;
                case R.id.b_block:
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.app_name)
                            .setMessage(getString(R.string.message_etes_vous_sur_bloquer_les_messages) + t_user.getText().toString())
                            .setPositiveButton(R.string.text_ok, (dialogInterface, i) -> {
                                appApi.doBlock(himId).enqueue(new Callback<ApiResponse>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                        if (response.isSuccessful()) {
                                            if (!response.body().isError()) {
                                                if (response.body().getMessage().equals("1;0") || response.body().getMessage().equals("1;1")) {
                                                    Alert(R.string.text_messages_bloques);
                                                    popup.getMenu().findItem(R.id.b_block).setTitle(R.string.text_autoriser);
                                                    input.setVisibility(View.GONE);
                                                } else if (response.body().getMessage().equals("0;1")) {
                                                    Alert(R.string.message_autorise_mais_vous_nepuvezpas_envoye);
                                                    popup.getMenu().findItem(R.id.b_block).setTitle(R.string.text_bloque);
                                                    input.setVisibility(View.GONE);
                                                } else {
                                                    Alert(R.string.text_messages_autorises);
                                                    popup.getMenu().findItem(R.id.b_block).setTitle(R.string.text_bloque);
                                                    if (mTache != null && mTache.getStatus() == 0)
                                                      input.setVisibility(View.VISIBLE);

                                                }
                                            } else {
                                                Alert(response.body().getMessage());
                                            }
                                        } else
                                            Alert(response.errorBody().toString());
                                    }

                                    @Override
                                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                                    }
                                });
                            })
                            .setNegativeButton(R.string.text_cancel, null).show();
                    break;
                case R.id.b_signaler:
                    if (finalId > 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle(R.string.signaler_cette_conversation);

                        final EditText input = new EditText(this);

                        builder.setView(input);

                        builder.setPositiveButton(R.string.text_signaler, (dialog, which) -> {
                            String s = input.getText().toString();
                            OptimTools.hideKeyboard(ChatActivity.this);

                            appApi.spam(finalId, input.getText().toString()).enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    if (response.isSuccessful()) {
                                        if (!response.body().isError()) {
                                            Alert(R.string.conversation_signalee);
                                        } else {
                                            Alert(response.body().getMessage());
                                        }
                                    } else
                                        Alert(response.errorBody().toString());
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {

                                }
                            });

                        });
                        builder.show();

                        input.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


                    } else {
                        Alert(R.string.text_veuillez_ressayer);
                    }
                    break;
            }
            return true;
        });

        popup.show();//showing popup menu
    }


    public void fixScroll() {
        lst.scrollToPosition(adapter.getItemCount() - 1);
    }

    @OnClick(R.id.b_send)
    public void performSend() {
        if (t_message.getText() == null || t_message.getText().toString().matches(""))
            return;
        String message = t_message.getText().toString();

        Chat mChat = new Chat();
        mChat.setMessage(message);
        createMessage(mChat, 0);

    }

    @OnClick(R.id.btn_photo)
    public void selectPhoto() {
        PickImageDialog.build(new PickSetup()
                .setTitle(this.getString(R.string.text_choose_avatar))
                .setTitleColor(R.color.colorPrimaryDark)
                .setGalleryButtonText(getString(R.string.galerie))
                .setCameraButtonText(getString(R.string.camera))
                .setCancelText(this.getString(R.string.text_cancel))
                .setPickTypes(EPickType.GALLERY, EPickType.CAMERA)
                .setButtonOrientation(LinearLayout.HORIZONTAL)
                .setSystemDialog(false))
                .setOnPickResult(r -> {
                    if (r.getError() == null) {

                        Chat mChat = new Chat();
                        mChat.setMessage(r.getPath());
                        createMessage(mChat, 1);

                    } else {
                        Alert(r.getError().getMessage());
                    }
                })
                .setOnPickCancel(this::finish).show(this.getSupportFragmentManager());
    }


    @OnClick(R.id.btn_position)
    public void selectPosition() {
        MapHelper.getInstance().getGPS(this, (pos) -> {


            if (pos == null)
                return;


            Chat mChat = new Chat();
            mChat.setMessage(pos.latitude + "$€$" + pos.longitude);
            createMessage(mChat, 2);

        });
    }

    @Override
    public void onSelectDateItem(Date datetime) {
        Chat mChat = new Chat();
        mChat.setMessage(OptimTools.dateTimeToFRString(datetime));
        createMessage(mChat, 3);
    }

    @OnClick(R.id.btn_montant)
    public void selectAmount() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.text_montant);

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton(R.string.text_envoyer, (dialog, which) -> {
            String s = input.getText().toString();
            OptimTools.hideKeyboard(ChatActivity.this);

            Chat mChat = new Chat();
            mChat.setMessage(s);
            createMessage(mChat, 4);

            dialog.dismiss();

        });
        builder.show();

        input.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }


    @OnClick(R.id.b_datetime)
    public void selectDateTime() {

        new RappelFragment(this).show(this.getSupportFragmentManager(), RappelFragment.class.getSimpleName());
    }


    @Override
    public void onItemClick(Chat chat) {
        if (chat.getType() == 2) {
            try {

                String[] pos = chat.getMessage().split("\\$€\\$");
                double myLatitude =OptimTools.toDouble(pos[0]);
                double myLongitude = Double.parseDouble(pos[1]);

                String geoUriString = "geo:" + myLatitude + "," + myLongitude + "?q=(Position)@" + myLatitude + "," + myLongitude;
                Uri geoUri = Uri.parse(geoUriString);
                Intent mapCall = new Intent(Intent.ACTION_VIEW, geoUri);
                startActivity(mapCall);
            } catch (Exception ex) {

            }

        } else if (chat.getType() == 3) {
            Intent calIntent = new Intent(Intent.ACTION_INSERT);
            calIntent.setType("vnd.android.cursor.item/event");
            Date date = OptimTools.toDateTimefromFR(chat.getMessage());
            GregorianCalendar calDate = new GregorianCalendar(date.getYear() + 1900, date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds());

            calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                    calDate.getTimeInMillis());
            calDate.add(Calendar.HOUR_OF_DAY, 1);
            calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                    calDate.getTimeInMillis());
            startActivity(calIntent);
        } else if (chat.getType() == 1) {
            OptimTools.previewPhoto(chat.getChatImage(), this);
        }
    }


    @Override
    public void doRefreshOnNotificationThread(Integer him, Integer badge) {
        if (him != himId)
            return;

        new Handler(getMainLooper()).post(this::RefreshChat);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 10033) {
            setResult(10033);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        LinearLayout bottom_sheet = findViewById(R.id.bottom_sheet);
        BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
            super.onBackPressed();
    }

    private void RefreshChat() {
        getConversation();
    }

    private void createMessage(Chat mChat, int type) {


        mChat.setService_id(service_id);
        if (mTache != null && mTache.getBon_id() > 0)
            mChat.setBon_id(mTache.getBon_id());
        mChat.setType(type);
        mChat.setIsme(1);
        mChat.setStamp(OptimTools.dateTimeToFRString(new Date()));
        sendMessage(mChat);
    }
}
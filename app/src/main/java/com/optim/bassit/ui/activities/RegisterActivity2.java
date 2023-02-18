package com.optim.bassit.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.di.modules.NetworkModule;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Customer;
import com.optim.bassit.ui.dialogs.ChooseDialogFragment;
import com.optim.bassit.utils.OptimTools;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity2 extends BaseActivity {

    String mobile, email, password, inviteCode, macAddr, image = "", encodeImageString = "";;
    private static final int GALLERY_ADD_PROFILE = 1;
    private static final String URI_PHOTO = "Uri_photo";
    private Bitmap bitmap = null;

    CircleImageView img_user;
    RelativeLayout b_inscrire;
    EditText et_nom, et_prenom, et_description, et_address;
    MultipartBody.Part part;
    String lon = "", lat = "", address = "";

    @Inject
    AppApi appApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        init();
    }

    private void init() {
        mobile = getIntent().getExtras().getString("mobile");
        email = getIntent().getExtras().getString("email");
        password = getIntent().getExtras().getString("password");
        inviteCode = getIntent().getExtras().getString("invite_code");
        macAddr = getIntent().getExtras().getString("macAddr");

        img_user = findViewById(R.id.img_user_circle);
        b_inscrire = findViewById(R.id.b_inscrire);
        et_nom = findViewById(R.id.et_nom);
        et_prenom = findViewById(R.id.et_prenom);
        et_description = findViewById(R.id.et_description);
        et_address = findViewById(R.id.et_address);

        img_user.setOnClickListener(v -> showImage());

        b_inscrire.setOnClickListener(v -> {
            if (validate()){
                registerUser();
            }

        });

        et_address.setOnClickListener(view -> getAddress());
    }

    private void registerUser() {
        Customer customer = new Customer(et_nom.getText().toString(), et_prenom.getText().toString(), mobile, email, password,getcode(inviteCode),macAddr);
        customer.setAddress(et_address.getText().toString());
        customer.setDescription(et_description.getText().toString());
        customer.setImage(image);
        customer.setMylat(lat);
        customer.setMylon(lon);
        show();
        appApi.registerCustomer(customer).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                hide();
                if (response.body() == null) {
                    Alert(R.string.error_server);
                    return;
                }
                if (!response.isSuccessful() || response.body().isError()) {
                    Alert(response.body().getMessage());
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    ChooseDialogFragment editNameDialogFragment = ChooseDialogFragment.newInstance(0, null,RegisterActivity2.this);
                    editNameDialogFragment.show(fm, "fragment_edit_name");
                    AlertWait(R.string.message_registration_success, () -> {
                        LoginActivity.doLogin(response, RegisterActivity2.this, appApi);
                    });

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hide();
                if (t == null)
                    return;
                Alert(t.getMessage());
            }
        });
    }

    private void uploadImage() {
        BaseActivity ba = this;
        appApi.uploadImage(part).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                hide();
                if (!response.isSuccessful())
                    ba.Alert(R.string.error_server);
                else if (response.body().isError())
                    ba.Alert(response.body().getMessage());
                else {
                    image = response.body().getMessage();
                    OptimTools.getPicasso(NetworkModule.PIN_URL + response.body().getMessage()).into(img_user);
                    FragmentManager fm = getSupportFragmentManager();
                    ChooseDialogFragment editNameDialogFragment = ChooseDialogFragment.newInstance(0, null,RegisterActivity2.this);
                    editNameDialogFragment.show(fm, "fragment_edit_name");
                    AlertWait(R.string.message_registration_success, () -> {
                        LoginActivity.doLogin(response, RegisterActivity2.this, appApi);
                    });
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Alert(t.getMessage());
                hide();
            }
        });
    }

    private boolean validate() {
        if (et_nom.getText().toString().matches("")){
            Toast.makeText(this, "Remplir le champs du nom Svp", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_prenom.getText().toString().matches("")){
            Toast.makeText(this, "Remplir le champs du prénom Svp", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_description.getText().toString().matches("")){
            Toast.makeText(this, "Remplir le champs du description Svp", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_address.getText().toString().matches("")){
            Toast.makeText(this, "Remplir le champs d'adresse Svp", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showImage() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, GALLERY_ADD_PROFILE);

//        PickImageDialog.build(new PickSetup()
//                .setTitle(this.getString(R.string.text_choose_avatar))
//                .setTitleColor(R.color.colorPrimaryDark)
//                .setGalleryButtonText(getString(R.string.galerie))
//                .setCameraButtonText(getString(R.string.camera))
//                .setCancelText(this.getString(R.string.text_cancel))
//                .setPickTypes(EPickType.GALLERY, EPickType.CAMERA)
//                .setButtonOrientation(LinearLayout.HORIZONTAL)
//                .setSystemDialog(false))
//                .setOnPickResult(r -> {
//                    if (r.getError() == null) {
//
//                        show();
//
//
//                        File file1 = new File(r.getPath());
//                        File file = OptimTools.resizeFile(file1, 500, this);
//
//                        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
//                        part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
//
//                        OptimTools.getPicasso(r.getPath()).into(img_user);
//
//                        hide();
//
//
//                    } else {
//                        //Handle possible errors
//                        //TODO: do what you have to do with r.getError();
//                        Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                })
//                .setOnPickCancel(() -> {
//                    //TODO: do what you have to if user clicked cancel
//                }).show(this);
    }

    private String getcode(String inv) {
        int b=0;
        if(!inv.isEmpty() && inv.matches("[0-9a-fA-F]+"))
            b= Integer.parseInt(inv,16);
        if(b==0)  return "";
        else   return b+"";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_ADD_PROFILE && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            img_user.setImageURI(imgUri);

            try {
                InputStream inputStream = getContentResolver().openInputStream(imgUri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                img_user.setImageBitmap(bitmap);
                bitmapToString(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 104) {
            address = data.getStringExtra("address");
            lon = data.getStringExtra("longitude");
            lat = data.getStringExtra("latitude");

            if (!address.matches("")){
                et_address.setText(address);
            }
        }
    }

    private void bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] array = byteArrayOutputStream.toByteArray();
        Log.i(URI_PHOTO, "bitmapToString: " + Base64.encodeToString(array, Base64.DEFAULT));

        File f = new File(getCacheDir(), "image");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        try {
            fos.write(array);
            fos.flush();
            fos.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), f);
        part = MultipartBody.Part.createFormData("image", f.getName(), fileReqBody);

       // encodeImageString = Base64.encodeToString(array, Base64.DEFAULT);

    }

    private void getAddress(){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("user", false);
        intent.putExtra("new", true);
        startActivityForResult(intent, 104);
    }


}
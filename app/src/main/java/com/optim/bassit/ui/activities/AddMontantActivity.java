package com.optim.bassit.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.utils.OptimTools;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;

import java.io.File;
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

public class AddMontantActivity extends BaseActivity {

    @BindView(R.id.b_back)
    ImageButton Back_btn;

    @BindView(R.id.t_montant)
    EditText t_montant;

    @BindView(R.id.t_note)
    EditText t_note;

    @BindView(R.id.img_recu)
    ImageView img_recu;


    @Inject
    AppApi appApi;

    List<MultipartBody.Part> servicePhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_montant);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        Back_btn.setOnClickListener(v -> finish());

    }

    boolean bsous;
    List<Categorie> all;

    @OnClick({R.id.b_choose, R.id.img_recu})
    public void AddImage() {
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

                        File imgFile = new File(r.getPath());

                        if (imgFile.exists()) {
                            fullpath = r.getPath();
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            img_recu.setImageBitmap(myBitmap);
                        }

                    } else {
                        Alert(r.getError().getMessage());
                    }
                })
                .setOnPickCancel(() -> finish()).show(this.getSupportFragmentManager());
    }

    String fullpath;


    @OnClick({R.id.b_confirmer})
    public void onConfirmerClick() {


        Integer montant = 0;

        try {
            montant = Integer.valueOf(t_montant.getText().toString());

        } catch (Exception ex) {
            Alert(R.string.message_fill_required_fields);
        }

        String note = t_note.getText().toString();

        if (montant == 0) {
            Alert(R.string.message_fill_required_fields);
            return;
        }


        File file1 = new File(fullpath);
        File file = OptimTools.resizeFile(file1, 1000, this);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imageRequest = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
        show();
        appApi.addMontant(note, montant, imageRequest).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                hide();
                if (!response.isSuccessful())
                    Alert(response.errorBody().toString());
                else if (response.body().isError()) {
                    Alert(response.body().getMessage());
                } else {
                    AddMontantActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hide();
                Alert(t.getMessage());

            }
        });


    }


    @OnClick({R.id.b_cancel})
    public void onAnnulerClick() {
        this.finish();
    }


}

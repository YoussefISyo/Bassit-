package com.optim.bassit.ui.adapters;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.di.modules.NetworkModule;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Commission;
import com.optim.bassit.models.DemandService;
import com.optim.bassit.ui.activities.DemandDetailsActivity;
import com.optim.bassit.utils.OptimTools;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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

public class CommissionAdapter extends RecyclerView.Adapter<CommissionAdapter.ViewHolder> {

    private List<Commission> mData;
    Context mcontext;
    private static final int GALLERY_ADD_PROFILE = 1;
    ImageView img_paiement;
    Bitmap bitmap;
    String encodeImageString = "";
    String featured_path = "";

    AppApi appApi;
    BottomSheetDialog bottomSheetDialog;

    public CommissionAdapter(Context mcontext, AppApi appApi) {
        this.mData = new ArrayList<>();
        this.mcontext = mcontext;
        this.appApi = appApi;
    }

    public void fill(List<Commission> commissionList) {
        mData.clear();
        mData.addAll(commissionList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommissionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commission_item, parent, false);

        return new CommissionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommissionAdapter.ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Commission commission;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @SuppressLint("RestrictedApi")
        public void bind(Commission commission) {
            this.commission = commission;

            TextView t_client = itemView.findViewById(R.id.t_client_name);
            TextView t_title = itemView.findViewById(R.id.t_title);
            TextView t_date = itemView.findViewById(R.id.t_date);
            CircleImageView avatar = itemView.findViewById(R.id.img_avatar);
            FrameLayout frameLayout = itemView.findViewById(R.id.frame_layout);
            CardView cardView = itemView.findViewById(R.id.tache_item);
            TextView txt_montant = itemView.findViewById(R.id.txt_montant);
            TextView txt_percent = itemView.findViewById(R.id.txt_percent);
            TextView txt_commission = itemView.findViewById(R.id.txt_commission);
            Button btn_paied = itemView.findViewById(R.id.btn_paied);

            t_title.setText(commission.getService_title());
            t_date.setText(commission.getDate());


            t_client.setText(CurrentUser.getInstance().getmCustomer().getUserName());
            OptimTools.getPicasso(CurrentUser.getInstance().getAvatar()).into(avatar);
            txt_montant.setText(String.valueOf(commission.getPrice()));
            txt_commission.setText(commission.getCommission());

            if (commission.getPaied() == 1){
                btn_paied.setVisibility(View.GONE);
            }

            if (CurrentUser.getInstance().getmCustomer().getPlan() == 101) {
                txt_percent.setText(R.string.bronze_percent);
            } else if (CurrentUser.getInstance().getmCustomer().getPlan() == 102) {
                txt_percent.setText(R.string.silver_percent);
            } else if (CurrentUser.getInstance().getmCustomer().getPlan() == 103) {
                txt_percent.setText(R.string.gold_percent);
            } else if (CurrentUser.getInstance().getmCustomer().getPlan() == 104) {
                txt_percent.setText(R.string.diamond_percent);
            } else {
                txt_percent.setText(R.string.normal_percent);
            }

            btn_paied.setOnClickListener(view -> showBottomSheet(commission.getId()));

        }
    }

    public void showBottomSheet(String id) {
        bottomSheetDialog = new BottomSheetDialog(mcontext, R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.dialog_commission_image);

        img_paiement = bottomSheetDialog.findViewById(R.id.img_paiement);
        Button btn_confirm = bottomSheetDialog.findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(view -> {

            MultipartBody.Part imageRequest = prepareFilePart("featured", featured_path);
            appApi.paiedCommission(id, imageRequest).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    Toast.makeText(mcontext, "le réçu va etre examiné par l'admin", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {

                }
            });
            bottomSheetDialog.dismiss();

        });

        img_paiement.setOnClickListener(view -> {

            PickImageDialog.build(new PickSetup()
                    .setTitle(mcontext.getString(R.string.t_une_photo_pour_le_service))
                    .setTitleColor(R.color.colorPrimaryDark)
                    .setGalleryButtonText(mcontext.getString(R.string.galerie))
                    .setCameraButtonText(mcontext.getString(R.string.camera))
                    .setCancelText(mcontext.getString(R.string.text_cancel))
                    .setPickTypes(EPickType.GALLERY, EPickType.CAMERA)
                    .setButtonOrientation(LinearLayout.HORIZONTAL)
                    .setSystemDialog(false))
                    .setOnPickResult(r -> {
                        if (r.getError() == null) {

                            File imgFile = new File(r.getPath());

                            if (imgFile.exists()) {
                                featured_path = imgFile.getAbsolutePath();
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                img_paiement.setImageBitmap(myBitmap);
                            }

                        } else {
                            //Alert(r.getError().getMessage());
                        }
                    })
                    .setOnPickCancel(() -> {}).show((FragmentActivity) mcontext);

        });

        bottomSheetDialog.show();
    }



    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == GALLERY_ADD_PROFILE && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            img_paiement.setImageURI(imgUri);

            try {
                InputStream inputStream = mcontext.getContentResolver().openInputStream(imgUri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                img_paiement.setImageBitmap(bitmap);
                bitmapToString(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] array = byteArrayOutputStream.toByteArray();

        encodeImageString = Base64.encodeToString(array, Base64.DEFAULT);

    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String fileUri) {

        if (fileUri.equals("*")) {
            return MultipartBody.Part.createFormData(partName, fileUri);
        } else {
            File file1 = new File(fileUri);
            File file = OptimTools.resizeFile(file1, 1000, mcontext);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        }
        // MultipartBody.Part is used to send also the actual file name

    }

}

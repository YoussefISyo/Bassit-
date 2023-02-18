package com.optim.bassit.ui.adapters;

import static com.optim.bassit.ui.activities.MainActivity.context;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.optim.bassit.R;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.DemandService;
import com.optim.bassit.models.Tache;
import com.optim.bassit.utils.OptimTools;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DemandsServiceAdapter extends RecyclerView.Adapter<DemandsServiceAdapter.ViewHolder> {

    private List<DemandService> mData;
    private DemandsServiceAdapter.ItemClickListener mListener;
    Context mcontext;
    AppApi appApi;

    public DemandsServiceAdapter(DemandsServiceAdapter.ItemClickListener listener, Context mcontext, AppApi appApi) {
        this.mListener = listener;
        this.mData = new ArrayList<>();
        this.mcontext=mcontext;
        this.appApi = appApi;
    }

    public void fill(List<DemandService> demandServiceList) {
        mData.clear();
        mData.addAll(demandServiceList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DemandsServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taches_item, parent, false);
        return new DemandsServiceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DemandsServiceAdapter.ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private DemandService demandService;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @SuppressLint("RestrictedApi")
        public void bind(DemandService demandService) {
            this.demandService = demandService;
            itemView.setOnClickListener(view -> mListener.onItemClick(demandService));

            TextView t_client = itemView.findViewById(R.id.t_client_name);
            TextView t_title = itemView.findViewById(R.id.t_title);
            TextView t_date = itemView.findViewById(R.id.t_date);
            CircleImageView avatar = itemView.findViewById(R.id.img_avatar);
            FrameLayout frameLayout = itemView.findViewById(R.id.frame_layout);
            CardView cardView = itemView.findViewById(R.id.tache_item);
            ImageView img_share = itemView.findViewById(R.id.img_share);
            TextView t_etat = itemView.findViewById(R.id.t_etat);

            t_title.setText(demandService.getService_title());
            t_date.setText(demandService.getDate_start());

            if (CurrentUser.getInstance().isPro()){
                t_client.setText(demandService.getUser_name());
                demandService.setPro_name(CurrentUser.getInstance().getmCustomer().getUserName());
                demandService.setImage_pro(CurrentUser.getInstance().getAvatar());
                OptimTools.getPicasso(demandService.getClientPinLink()).into(avatar);
                demandService.setPro_id(CurrentUser.getInstance().getmCustomer().getId());
            }else{
                t_client.setText(demandService.getPro_name());
                OptimTools.getPicasso(demandService.getProPinLink()).into(avatar);
                demandService.setUser_name(CurrentUser.getInstance().getmCustomer().getUserName());
                demandService.setImage_user(CurrentUser.getInstance().getAvatar());
                demandService.setUser_id(CurrentUser.getInstance().getmCustomer().getId());
            }

            if (demandService.getState() == 2){
                img_share.setVisibility(View.VISIBLE);
                t_etat.setVisibility(View.GONE);
            }else if (demandService.getState() == 3){
                img_share.setVisibility(View.GONE);
                t_etat.setVisibility(View.VISIBLE);
            }else{
                img_share.setVisibility(View.GONE);
                t_etat.setVisibility(View.GONE);
            }

            img_share.setOnClickListener(v -> {
                if (CurrentUser.getInstance().isPro()){
                    showDialog(demandService);
                }else{
                    checkRating(demandService);

                }


            });


//            BadgeDrawable badgeDrawable = BadgeDrawable.create(mcontext);
//
//            badgeDrawable.setVisible(true);
//            BadgeUtils.attachBadgeDrawable(badgeDrawable,cardView, frameLayout);

        }
    }

    private void checkRating(DemandService demandService) {

        appApi.checkRating(String.valueOf(demandService.getService_id()), String.valueOf(CurrentUser.getInstance().getmCustomer().getId())).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        showDialogEvaluation(demandService);
                    }else{
                        Toast.makeText(mcontext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    private void showDialogEvaluation(DemandService demandService) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        final View customLayout = ((Activity)mcontext).getLayoutInflater().inflate(R.layout.dialog_rating, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        RatingBar rating_bar = customLayout.findViewById(R.id.rating_bar);
        EditText txt_comment = customLayout.findViewById(R.id.txt_comment);
        Button btn_evaluate = customLayout.findViewById(R.id.btn_evaluate);

        btn_evaluate.setOnClickListener(view -> {
            evaluateUser(demandService, rating_bar.getRating(), txt_comment.getText().toString());
            dialog.dismiss();
        });

        customLayout.findViewById(R.id.btn_cancel).setOnClickListener(v1 -> {
            dialog.dismiss();

        });
    }

    private void evaluateUser(DemandService demandService, float rating, String comment) {
        appApi.addRating(demandService.getService_title(), String.valueOf(demandService.getService_id()),String.valueOf(CurrentUser.getInstance().getmCustomer().getId()),
                rating, comment, "")
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                new android.app.AlertDialog.Builder(context)
                                        .setTitle(R.string.app_name)
                                        .setMessage(context.getString(R.string.tach_fini))
                                        .setPositiveButton(R.string.text_ok, (dialogInterface, i) -> {
                                            dialogInterface.dismiss();

                                        }).show();

                            } else {
                                OptimTools.Alert(context, response.body().getMessage());
                            }
                        } else
                            Toast.makeText(context, "is not successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        OptimTools.Alert(context, t.getMessage());
                    }
                });
    }

    private void showDialog(DemandService demandService) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        final View customLayout = ((Activity)mcontext).getLayoutInflater().inflate(R.layout.dialog_share, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        CircleImageView img_user = customLayout.findViewById(R.id.img_user);
        TextView txt_username = customLayout.findViewById(R.id.txt_username);
        TextView txt_city = customLayout.findViewById(R.id.txt_city);
        TextView txt_title = customLayout.findViewById(R.id.txt_title);
        TextView txt_category = customLayout.findViewById(R.id.txt_category);
        TextView txt_unity = customLayout.findViewById(R.id.txt_unity);
        TextView txt_quantity = customLayout.findViewById(R.id.txt_quantity);
        TextView txt_startDate = customLayout.findViewById(R.id.txt_startDate);
        TextView txt_endDate = customLayout.findViewById(R.id.txt_endDate);

        OptimTools.getPicasso(CurrentUser.getInstance().getAvatar()).into(img_user);

        txt_username.setText(demandService.getPro_name());
        txt_city.setText(demandService.getCity());
        txt_title.setText(demandService.getService_title());
        txt_category.setText(demandService.getCategory());
        txt_unity.setText(demandService.getUnity());
        txt_quantity.setText(String.valueOf(demandService.getQuantity()));
        txt_startDate.setText(demandService.getDate_start() + " /");
        txt_endDate.setText(demandService.getDate_end());

        customLayout.findViewById(R.id.btn_share).setOnClickListener(v1 -> {
            shareDemand(demandService);
            dialog.dismiss();

        });
    }

    private void shareDemand(DemandService demandService) {

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Service Terminé avec Succés");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, mcontext.getString(R.string.finish_service) + demandService.getService_title() + mcontext.getString(R.string.thanks_to) +
                    mcontext.getString(R.string.download_it) +" https://bassit-app.com/download");
            mcontext.startActivity(Intent.createChooser(sharingIntent, mcontext.getString(R.string.partager_sur)));

    }

    public interface ItemClickListener {
        void onItemClick(DemandService demandService);
    }
}

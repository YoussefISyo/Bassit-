package com.optim.bassit.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.ui.activities.EditInformationActivity;
import com.optim.bassit.ui.activities.ServiceAddEditActivity;
import com.optim.bassit.ui.activities.SortieBonusActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseDialogFragment extends DialogFragment {

    @Inject
    AppApi appApi;


    Dialog mDialog;
    private int type;
    private doRefreshListener mlistener;

    public ChooseDialogFragment() {
        // Required empty public constructor
    }
Context mcontext;
    public static ChooseDialogFragment newInstance(int type, doRefreshListener mlistener,Context mcontext) {
        ChooseDialogFragment fragment = new ChooseDialogFragment();
        fragment.mcontext=mcontext;
        fragment.type = type;
        fragment.mlistener = mlistener;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.dialog_fragment_choose, container, false);
        ButterKnife.bind(this, vw);
        mDialog = new Dialog(this.getContext());
        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        if (type == 101) {
            ((TextView) vw.findViewById(R.id.t_business)).setText(R.string.text_transferer_bonus);
            ((TextView) vw.findViewById(R.id.t_client)).setText(R.string.text_retrait_ccp);

            ((ImageView) vw.findViewById(R.id.i_business)).setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_compare_arrows));
            ((ImageView) vw.findViewById(R.id.i_client)).setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_money));

        } else {
          //  vw.findViewById(R.id.b_afrili).setVisibility(View.VISIBLE);
        }

        return vw;
    }

    @OnClick(R.id.b_business)
    public void businessClick() {

        if (type == 101) //transfert
        {
            Intent intent = new Intent(getActivity(), ServiceAddEditActivity.class);
//            intent.putExtra("isclient", false);
//            getActivity().startActivityForResult(intent, 10066);
            startActivity(intent);
            dismiss();
            return;
        }
        appApi.doPro().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (mlistener != null)
                        mlistener.doRefresh();

                    else{
                        try{
                        SharedPreferences prefs = mcontext.getSharedPreferences("bassitpro",0);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("choose", "false");
                        editor.apply();}
                        catch (Exception e){}
                        restartApplication();
                    }

                }
                dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dismiss();
            }
        });
        dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10066)
            if (mlistener != null)
                mlistener.doRefresh();
            else
                restartApplication();
    }

    @OnClick(R.id.b_afrili)
    public void afriliClick() {

        appApi.doAfrili().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful())
                    if (mlistener != null)
                        mlistener.doRefresh();
                    else
                        restartApplication();
                dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dismiss();
            }
        });
        dismiss();
    }


    @OnClick(R.id.b_client)
    public void clientClick() {
        if (type == 101) //retrait ccp
        {
            if (CurrentUser.getInstance().getmCustomer().getCcp() == null || CurrentUser.getInstance().getmCustomer().getCcp().matches("") ||
                    CurrentUser.getInstance().getmCustomer().getNomprenomccp() == null || CurrentUser.getInstance().getmCustomer().getNomprenomccp().matches("")) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.veuillez_remplir_ccp)
                        .setPositiveButton(R.string.text_confirm, (a, b) -> {
                            Intent intent = new Intent(getActivity(), EditInformationActivity.class);
                            getActivity().startActivity(intent);
                            a.dismiss();
                        })
                        .setNegativeButton(R.string.text_cancel, null).show();
                return;
            }
            Intent intent = new Intent(getActivity(), SortieBonusActivity.class);
            intent.putExtra("isclient", true);
            getActivity().startActivityForResult(intent, 10066);
            dismiss();
            return;
        }
        appApi.doClient().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful())
                    if (mlistener != null)
                        mlistener.doRefresh();
                    else
                        restartApplication();

                dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dismiss();
            }
        });
        dismiss();
    }

    Activity ctx_caller;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


        if (context instanceof Activity) {
            ctx_caller = (Activity) context;
        }

    }

    public interface doRefreshListener {
        void doRefresh();
    }


    public void restartApplication() {

        if (ctx_caller == null)
            return;


        Intent i = ctx_caller.getPackageManager().getLaunchIntentForPackage(ctx_caller.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx_caller.startActivity(i);
        ctx_caller.finish();


    }

}

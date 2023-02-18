package com.optim.bassit.ui.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseDialog;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.Journal;
import com.optim.bassit.ui.activities.FinanceActivity;
import com.optim.bassit.ui.fragments.TresorFragment;
import com.optim.bassit.utils.OptimTools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MontantDialogFragment extends BaseDialog {

    @Inject
    AppApi appApi;

    @BindView(R.id.t_date)
    EditText mDate;
    @BindView(R.id.sp_type)
    Spinner spType;
    @BindView(R.id.t_montant)
    EditText mMontant;
    @BindView(R.id.t_designation)
    EditText mDesignation;

    Dialog mDialog;
    public TresorFragment tresorFragment;
    private Journal journal;

    public MontantDialogFragment() {
        // Required empty public constructor
    }

    public static MontantDialogFragment newInstance(TresorFragment tresorFragment) {
        MontantDialogFragment fragment = new MontantDialogFragment();
        fragment.tresorFragment = tresorFragment;
        return fragment;
    }

    public static MontantDialogFragment newInstance(TresorFragment tresorFragment, Journal journal) {
        MontantDialogFragment fragment = new MontantDialogFragment();
        fragment.tresorFragment = tresorFragment;
        fragment.journal = journal;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.dialog_fragment_montant, container, false);
        ButterKnife.bind(this, vw);
        mDialog = new Dialog(this.getContext());
        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        List<String> list_tag = new ArrayList<String>();
        list_tag.add(getString(R.string.charge));
        list_tag.add(getString(R.string.revenu));
        ArrayAdapter<String> dataAdapter_type = new ArrayAdapter<String>(vw.getContext(),
                android.R.layout.simple_spinner_item, list_tag);
        dataAdapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(dataAdapter_type);

        final Calendar cldr = Calendar.getInstance();

        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int mnth = cldr.get(Calendar.MONTH)+1;
        int y = cldr.get(Calendar.YEAR);
        mDate.setInputType(InputType.TYPE_NULL);
        mDate.setFocusable(false);
        mDate.setText(String.format("%02d/%02d/%d", day, mnth, y));

        mDate.setOnClickListener(v -> {
            DatePickerDialog picker = new DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> mDate.setText(String.format("%02d/%02d/%d", dayOfMonth, month+1, year)), y, mnth-1, day);
            picker.show();

        });

        if (journal != null) {
            if (journal.getMontant() > 0)
                spType.setSelection(1);
            else
                spType.setSelection(0);

            mDate.setText(OptimTools.dateToFRString(OptimTools.toDateTime(journal.getStamp())));
            mMontant.setText(new DecimalFormat("#").format(Math.abs(journal.getMontant())));
            mDesignation.setText(journal.getDesignation());
        }

        return vw;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @OnClick(R.id.btn_cancel)
    public void cancelClick() {
        dismiss();
    }

    @OnClick(R.id.btn_ok)
    public void performAdd() {
        int type = spType.getSelectedItemPosition();
        Double montant = Double.parseDouble(mMontant.getText().toString());
        String date = OptimTools.dateToSQLString(OptimTools.toDate(mDate.getText().toString()));
        String des = mDesignation.getText().toString();
        if (mMontant.getText().toString().matches("") || montant == 0) {
            OptimTools.Alert(MontantDialogFragment.this.getContext(), R.string.message_fill_required_fields);
            return;
        }

        appApi.addEditTresor(date, des, type == 0 ? -montant : montant, journal == null ? 0 : journal.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!response.isSuccessful() || response.body().isError()) {
                    OptimTools.Alert(mDialog.getContext(), response.message());
                } else {

                    ((FinanceActivity) tresorFragment.getActivity()).setBreload();
                    dismiss();
                    new AlertDialog.Builder(tresorFragment.getActivity())
                            .setTitle(R.string.app_name)
                            .setMessage((journal == null ? getString(R.string.ajout_termine) : getString(R.string.modification_terminee)) +" "+ getString(R.string.avec_success))
                            .setPositiveButton(R.string.text_ok, (dialog, which) -> MontantDialogFragment.this.dismiss())
                            .show();

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                OptimTools.Alert(MontantDialogFragment.this.getContext(), t.getMessage());
            }
        });

    }


}

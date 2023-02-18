package com.optim.bassit.ui.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.utils.OptimTools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("ValidFragment")
public class RappelFragment extends DialogFragment {

    private final ItemClickListener itemClickListener;
    @Inject
    AppApi appApi;

    @BindView(R.id.t_time)
    EditText mTime;
    @BindView(R.id.t_date)
    EditText mDate;

    public RappelFragment(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_rappel, container, false);

        ButterKnife.bind(this, vw);
        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        final Calendar cldr = Calendar.getInstance();
        int h = cldr.get(Calendar.HOUR_OF_DAY);
        int m = cldr.get(Calendar.MINUTE);

        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int mnth = cldr.get(Calendar.MONTH);
        int y = cldr.get(Calendar.YEAR);

        mTime.setInputType(InputType.TYPE_NULL);
        mTime.setFocusable(false);

        mTime.setOnClickListener(v -> {
            TimePickerDialog picker = new TimePickerDialog(getActivity(), (view, hourOfDay, minute) -> mTime.setText(String.format("%02d:%02d", hourOfDay, minute)), h, m, true);
            picker.show();

        });

        mDate.setInputType(InputType.TYPE_NULL);
        mDate.setFocusable(false);

        mDate.setOnClickListener(v -> {
            DatePickerDialog picker = new DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> mDate.setText(String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)), y, mnth, day);
            picker.show();

        });


        return vw;
    }


    @OnClick(R.id.btn_cancel)
    public void cancelClick() {
        dismiss();
    }

    @OnClick(R.id.btn_ok)
    public void performAdd() {


        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(mDate.getText() + " " + mTime.getText());
        } catch (ParseException e) {


        }
        if (date == null) {
            OptimTools.Alert(getActivity(), R.string.error_invalid_date);
            return;
        }

        itemClickListener.onSelectDateItem(date);
        dismiss();

    }

    //****************** INTERFACE ***********************
    public interface ItemClickListener {
        void onSelectDateItem(Date datetime);
    }
}

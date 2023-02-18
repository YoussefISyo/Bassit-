package com.optim.bassit.base;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.optim.bassit.utils.OptimTools;


@SuppressLint("Registered")
public class BaseDialog extends DialogFragment {


    public void Alert(int message) {
        OptimTools.Alert(getActivity(), message);
    }

    public void Alert(String message) {
        OptimTools.Alert(getActivity(), message);
    }

    public void show(View vw) {
        vw.setVisibility(View.VISIBLE);
    }

    public boolean isempty(TextView vw) {
        try {

            if (vw.getText() == null || vw.getText().toString().matches(""))
                return true;
        } catch (Exception ex) {
            return true;
        }
        return false;
    }

    public void hide(View vw) {
        vw.setVisibility(View.GONE);
    }
}
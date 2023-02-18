package com.optim.bassit.base;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.optim.bassit.R;
import com.optim.bassit.utils.OptimTools;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;



@SuppressLint("Registered")
public class BaseFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void Alert(int message) {
        OptimTools.Alert(getActivity(), message);
    }
    public void AlertYesNo(int message, String ok_button, String cancel_button, OptimTools.AlertOkListener handler) {
        AlertYesNo(message, ok_button, cancel_button, handler);
    }
    public void Alert(String message) {
        OptimTools.Alert(getActivity(), message);
    }

    public void show(View vw2) {
        View vw =  vw2.findViewById(R.id.loading);
        OptimTools.animateView(vw, View.VISIBLE, 1, 200);
    }

    public void hide(View vw2) {
        View vw =  vw2.findViewById(R.id.loading);
        OptimTools.animateView(vw, View.GONE, 0, 200);
    }

    public void show() {
        View vw = getView().findViewById(R.id.loading);
        OptimTools.animateView(vw, View.VISIBLE, 1, 200);
    }


    public void hide() {
        View vw = getView().findViewById(R.id.loading);
        OptimTools.animateView(vw, View.GONE, 0, 200);
    }

    public void selectOnePhoto( ) {
        FishBun.with(this)
                .setImageAdapter(new GlideAdapter())
                .setMaxCount(1)
                .setMinCount(1)
                .setPickerSpanCount(5)
                .setActionBarColor(this.getResources().getColor(R.color.colorPrimaryDark), this.getResources().getColor(R.color.colorPrimaryDark), false)
                .setActionBarTitleColor(Color.parseColor("#ffffff"))
//                .setArrayPaths(path)
                .setAlbumSpanCount(1, 1)
                .setButtonInAlbumActivity(false)
                .setCamera(true)
                .exceptGif(true)
                .setReachLimitAutomaticClose(true)
                .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_arrow_back))
                .setDoneButtonDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_check))
                .setAllDoneButtonDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_check))
                .setAllViewTitle(this.getString(R.string.tous))
                .setMenuAllDoneText(this.getString(R.string.fini))
                .setActionBarTitle(this.getString(R.string.photos))
                .textOnNothingSelected(this.getString(R.string.veuillez_selectionner_au_moins_une_photo))
                .startAlbum();
    }
    public void selectPhotos( ) {
        FishBun.with(this)
                .setImageAdapter(new GlideAdapter())
                .setMaxCount(10)
                .setMinCount(1)
                .setPickerSpanCount(5)
                .setActionBarColor(this.getResources().getColor(R.color.colorPrimaryDark), this.getResources().getColor(R.color.colorPrimaryDark), false)
                .setActionBarTitleColor(Color.parseColor("#ffffff"))
//                .setArrayPaths(path)
                .setAlbumSpanCount(1, 1)
                .setButtonInAlbumActivity(false)
                .setCamera(true)
                .exceptGif(true)
                .setReachLimitAutomaticClose(false)
                .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_arrow_back))
                .setDoneButtonDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_check))
                .setAllDoneButtonDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_check))
                .setAllViewTitle(this.getString(R.string.tous))
                .setMenuAllDoneText(this.getString(R.string.fini))
                .setActionBarTitle(this.getString(R.string.photos))
                .textOnNothingSelected(this.getString(R.string.veuillez_selectionner_au_moins_une_photo))
                .startAlbum();
    }

}
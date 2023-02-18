package com.optim.bassit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.optim.bassit.R;
import com.optim.bassit.models.Photo;
import com.optim.bassit.utils.OptimTools;
import com.otaliastudios.zoom.ZoomImageView;

import butterknife.ButterKnife;


public class ImageFragment extends Fragment {


    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }



    Photo photo;

    public static ImageFragment newInstance(Photo ads) {
        ImageFragment fragment = new ImageFragment();
        fragment.setPhoto(ads);
        return fragment;
    }

    public ImageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_image, container, false);
        ButterKnife.bind(this, vw);
        ZoomImageView zoom = vw.findViewById(R.id.zoom);
        OptimTools.getPicasso(photo.getFullImage())
                .into(zoom);
        return vw;
    }

}

package com.optim.bassit.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Album implements Parcelable {


    private int id;
    private String title;

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    boolean ispreview;

    public boolean isIspreview() {
        return ispreview;
    }

    public void setIspreview(boolean ispreview) {
        this.ispreview = ispreview;
    }

    List<Photo> photos = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected Album(Parcel in) {
        id = in.readInt();
        title = in.readString();
    }

    public Album() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
}
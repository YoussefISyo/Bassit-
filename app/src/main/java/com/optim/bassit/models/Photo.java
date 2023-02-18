package com.optim.bassit.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.optim.bassit.di.modules.NetworkModule;

public class Photo implements Parcelable {


    private int id;
    private int album_id;

    public Photo(String url) {
        this.url = url;
    }

    protected Photo(Parcel in) {
        id = in.readInt();
        album_id = in.readInt();
        name = in.readString();
        album = in.readString();
        blocal = in.readByte() != 0;
        url=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(album_id);
        dest.writeString(name);
        dest.writeString(album);
        dest.writeByte((byte) (blocal ? 1 : 0));
        dest.writeString(url);
    }

    boolean ispreview;

    public boolean isIspreview() {
        return ispreview;
    }

    public void setIspreview(boolean ispreview) {
        this.ispreview = ispreview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getFullUrl(int size) {
        if (!blocal) {
            return NetworkModule.ALBUM_IMAGE_URL + name + "/" + size;
        } else
            return name;
    }

    public String getFullImage() {
        if(url!= null && !url.matches(""))
            return url;

        if (!blocal) {
            return NetworkModule.ALBUM_IMAGE_URL + name;
        } else
            return name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private String album;
    String url;
    public Photo() {

    }


    boolean blocal = false;

    public void setNameLocal(String photo_path) {
        blocal = true;
        name = photo_path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public boolean isBlocal() {
        return blocal;
    }

    public void setBlocal(boolean blocal) {
        this.blocal = blocal;
    }
}

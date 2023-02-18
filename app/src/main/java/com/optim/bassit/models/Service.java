package com.optim.bassit.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Service implements Parcelable {

    protected Service(Parcel in) {
        id = in.readInt();
        title = in.readString();
        souscategorie_id = in.readInt();
        gagner = in.readInt();
        description = in.readString();
        tag = in.readString();
        splan = in.readInt();
        plan = in.readInt();
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String title;
    private int souscategorie_id;
    private int gagner;
    private String description;
    private String tag;

    private int splan;
    private int plan;
    private String min_price;
    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public int getGagner() {
        return gagner;
    }

    public void setGagner(int gagner) {
        this.gagner = gagner;
    }

    public Service()
    {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSouscategorie_id() {
        return souscategorie_id;
    }

    public void setSouscategorie_id(int souscategorie_id) {
        this.souscategorie_id = souscategorie_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(souscategorie_id);
        dest.writeInt(gagner);
        dest.writeString(description);
        dest.writeString(tag);
        dest.writeInt(splan);
        dest.writeInt(plan);
    }
}

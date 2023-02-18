package com.optim.bassit.models;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.optim.bassit.R;

public class Journal implements Parcelable {


    private Integer id;
    private String designation;
    private String type;
    private double montant;
    private int etat;
    private String image;

    protected Journal(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        designation = in.readString();
        type = in.readString();
        montant = in.readDouble();
        etat = in.readInt();
        image = in.readString();
        stamp = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(designation);
        dest.writeString(type);
        dest.writeDouble(montant);
        dest.writeInt(etat);
        dest.writeString(image);
        dest.writeString(stamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Journal> CREATOR = new Creator<Journal>() {
        @Override
        public Journal createFromParcel(Parcel in) {
            return new Journal(in);
        }

        @Override
        public Journal[] newArray(int size) {
            return new Journal[size];
        }
    };

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    private String stamp;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getFullEtat(Context ctx) {
        if (etat == 1)
            return ctx.getString(R.string.succes);
        else if (etat == 2)
            return ctx.getString(R.string.fail);
        else if (etat == 0)
            return ctx.getString(R.string.text_attent);
        return "";
    }
}
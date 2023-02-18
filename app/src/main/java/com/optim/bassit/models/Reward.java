package com.optim.bassit.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.optim.bassit.data.AppData;

public class Reward implements Parcelable {


    private Integer id;
    private String designation;
    private String points;
    private String d_ar;
    private String d_en;
    private String oldprix;
    private String porso;

    public String getOldprix() {
        return oldprix;
    }

    public void setOldprix(String oldprix) {
        this.oldprix = oldprix;
    }

    public String getPorso() {
        return porso;
    }

    public void setPorso(String porso) {
        this.porso = porso;
    }

    public String getD_ar() {
        return d_ar;
    }

    public void setD_ar(String d_ar) {
        this.d_ar = d_ar;
    }

    public String getD_en() {
        return d_en;
    }

    public void setD_en(String d_en) {
        this.d_en = d_en;
    }

    protected Reward(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        designation = in.readString();
        points = in.readString();
        valeur = in.readInt();
        image = in.readString();
        ordre = in.readInt();
        sysimage = in.readInt();
        moneyunit = in.readInt();
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
        dest.writeString(points);
        dest.writeInt(valeur);
        dest.writeString(image);
        dest.writeInt(ordre);
        dest.writeInt(sysimage);
        dest.writeInt(moneyunit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Reward> CREATOR = new Creator<Reward>() {
        @Override
        public Reward createFromParcel(Parcel in) {
            return new Reward(in);
        }

        @Override
        public Reward[] newArray(int size) {
            return new Reward[size];
        }
    };

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    private int valeur;


    private String image;
    private int ordre;

    private int sysimage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesignation() {
        return AppData.TR(designation);
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public int getSysimage() {
        return sysimage;
    }

    public void setSysimage(int sysimage) {
        this.sysimage = sysimage;
    }

    public int getMoneyunit() {
        return moneyunit;
    }

    public void setMoneyunit(int moneyunit) {
        this.moneyunit = moneyunit;
    }

    private int moneyunit;


    public String getFullImage() {
        return "https://google.com/img.jpg";
    }
}
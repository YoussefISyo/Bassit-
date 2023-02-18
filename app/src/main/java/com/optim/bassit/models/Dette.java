package com.optim.bassit.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.optim.bassit.di.modules.NetworkModule;
import com.optim.bassit.utils.OptimTools;

public class Dette implements Parcelable {

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public double getPaye() {
        return paye;
    }

    public void setPaye(double paye) {
        this.paye = paye;
    }

    public double getMontant() {
        return montant;
    }

    public double getReste() {
        return montant - paye;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    private String fullname;
    private double charge;
    private double paye;
    private double montant;

    private String service;
    private String image;
    private String stamp;

    protected Dette(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        fullname = in.readString();
        charge = in.readDouble();
        paye = in.readDouble();
        montant = in.readDouble();
        service = in.readString();
        image = in.readString();
        stamp = in.readString();
    }

    public static final Creator<Dette> CREATOR = new Creator<Dette>() {
        @Override
        public Dette createFromParcel(Parcel in) {
            return new Dette(in);
        }

        @Override
        public Dette[] newArray(int size) {
            return new Dette[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(name);
        parcel.writeString(fullname);
        parcel.writeDouble(charge);
        parcel.writeDouble(paye);
        parcel.writeDouble(montant);
        parcel.writeString(service);
        parcel.writeString(image);
        parcel.writeString(stamp);
    }

    public String getFullImage() {
        if (image == null || image.matches(""))
            return NetworkModule.PIN_URL + "/1";
        return NetworkModule.PIN_URL + image;
    }

    public String getFullStamp() {
        if(stamp==null || stamp.matches(""))
            return "";
        return OptimTools.dateTimeToFRString(OptimTools.toDateTime(stamp));
    }
}
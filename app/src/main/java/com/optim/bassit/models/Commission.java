package com.optim.bassit.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.optim.bassit.di.modules.NetworkModule;

public class Commission implements Parcelable {

    String id;
    String id_demandService;
    String id_pro;
    String id_user;
    String commission;
    int paied;
    String price;
    String service_title;
    String user_name;
    String user_image;
    String created_at;

    public Commission(){}

    protected Commission(Parcel in) {
        id = in.readString();
        id_demandService = in.readString();
        id_pro = in.readString();
        id_user = in.readString();
        commission = in.readString();
        paied = in.readInt();
        price = in.readString();
        service_title = in.readString();
        user_name = in.readString();
        user_image = in.readString();
        created_at = in.readString();
    }

    public static final Creator<Commission> CREATOR = new Creator<Commission>() {
        @Override
        public Commission createFromParcel(Parcel in) {
            return new Commission(in);
        }

        @Override
        public Commission[] newArray(int size) {
            return new Commission[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_demandService() {
        return id_demandService;
    }

    public void setId_demandService(String id_demandService) {
        this.id_demandService = id_demandService;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public int getPaied() {
        return paied;
    }

    public void setPaied(int paied) {
        this.paied = paied;
    }

    public String getId_pro() {
        return id_pro;
    }

    public void setId_pro(String id_pro) {
        this.id_pro = id_pro;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getService_title() {
        return service_title;
    }

    public void setService_title(String service_title) {
        this.service_title = service_title;
    }

    public String getUsername() {
        return user_name;
    }

    public void setUsername(String username) {
        this.user_name = username;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUserPinLink() {
        String ig = NetworkModule.PIN_URL + getUser_image();
        return ig;
    }

    public String getDate() {
        return created_at;
    }

    public void setDate(String date) {
        this.created_at = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(id_demandService);
        dest.writeString(id_pro);
        dest.writeString(id_user);
        dest.writeString(commission);
        dest.writeInt(paied);
        dest.writeString(price);
        dest.writeString(service_title);
        dest.writeString(user_name);
        dest.writeString(user_image);
        dest.writeString(created_at);
    }
}

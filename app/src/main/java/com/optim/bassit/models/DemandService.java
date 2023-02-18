package com.optim.bassit.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.optim.bassit.di.modules.NetworkModule;

public class DemandService implements Parcelable {

    int id;
    int user_id;
    int pro_id;
    int service_id;
    String date_start;
    String date_end;
    String category;
    String city;
    int urgence;
    String unity;
    int quantity;
    int price;
    String code_promo;
    String description;
    int state;
    String user_name;
    String pro_name;
    String service_title;
    String image_pro;
    String image_user;
    int negociation;
    int official_account;
    int status;
    int time_extend;
    String phone;

    public DemandService(){}


    protected DemandService(Parcel in) {
        id = in.readInt();
        user_id = in.readInt();
        pro_id = in.readInt();
        service_id = in.readInt();
        date_start = in.readString();
        date_end = in.readString();
        category = in.readString();
        city = in.readString();
        urgence = in.readInt();
        unity = in.readString();
        quantity = in.readInt();
        price = in.readInt();
        code_promo = in.readString();
        description = in.readString();
        user_name = in.readString();
        pro_name = in.readString();
        service_title = in.readString();
        image_pro = in.readString();
        image_user = in.readString();
        state = in.readInt();
        negociation = in.readInt();
        official_account = in.readInt();
        status = in.readInt();
        time_extend = in.readInt();
        phone = in.readString();
    }

    public static final Creator<DemandService> CREATOR = new Creator<DemandService>() {
        @Override
        public DemandService createFromParcel(Parcel in) {
            return new DemandService(in);
        }

        @Override
        public DemandService[] newArray(int size) {
            return new DemandService[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(user_id);
        dest.writeInt(pro_id);
        dest.writeInt(service_id);
        dest.writeString(date_start);
        dest.writeString(date_end);
        dest.writeString(category);
        dest.writeString(city);
        dest.writeInt(urgence);
        dest.writeString(unity);
        dest.writeInt(quantity);
        dest.writeInt(price);
        dest.writeString(code_promo);
        dest.writeString(description);
        dest.writeString(user_name);
        dest.writeString(pro_name);
        dest.writeString(service_title);
        dest.writeString(image_pro);
        dest.writeString(image_user);
        dest.writeInt(state);
        dest.writeInt(negociation);
        dest.writeInt(official_account);
        dest.writeInt(status);
        dest.writeInt(time_extend);
        dest.writeString(phone);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPro_id() {
        return pro_id;
    }

    public void setPro_id(int pro_id) {
        this.pro_id = pro_id;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getUrgence() {
        return urgence;
    }

    public void setUrgence(int urgence) {
        this.urgence = urgence;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCode_promo() {
        return code_promo;
    }

    public void setCode_promo(String code_promo) {
        this.code_promo = code_promo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getService_title() {
        return service_title;
    }

    public void setService_title(String service_title) {
        this.service_title = service_title;
    }

    public String getImage_pro() {
        return image_pro;
    }

    public void setImage_pro(String image_pro) {
        this.image_pro = image_pro;
    }

    public String getImage_user() {
        return image_user;
    }

    public void setImage_user(String image_user) {
        this.image_user = image_user;
    }

    public String getClientPinLink() {
        String ig = NetworkModule.PIN_URL + getImage_user();
        return ig;
    }

    public String getProPinLink() {
        String ig = NetworkModule.PIN_URL + getImage_pro();
        return ig;
    }

    public int getNegociate() {
        return negociation;
    }

    public void setNegociate(int negociate) {
        this.negociation = negociate;
    }

    public int getOfficial_account() {
        return official_account;
    }

    public void setOfficial_account(int official_account) {
        this.official_account = official_account;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNegociation() {
        return negociation;
    }

    public void setNegociation(int negociation) {
        this.negociation = negociation;
    }

    public int getTime_extend() {
        return time_extend;
    }

    public void setTime_extend(int time_extend) {
        this.time_extend = time_extend;
    }

    public static Creator<DemandService> getCREATOR() {
        return CREATOR;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

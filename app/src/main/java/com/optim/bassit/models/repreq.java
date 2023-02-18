package com.optim.bassit.models;


import android.content.Context;

import com.optim.bassit.R;
import com.optim.bassit.data.AppData;
import com.optim.bassit.di.modules.NetworkModule;


public  class repreq {



    private int id;
    private  String des;
    private  String daterep;
    private  String type_s;
    private  String user_id;
    private  String req_id;
    private  String duration;
    private  String service_id;
    private  String url;
    private  String prority;
    private  String prix;
    private  String datereps;
    private  String adrs;
    private String date_start;
    private String unity;
    private int quantity;
    private String designation;

    /*  public HomeadsFeed() {
    }
    public HomeadsFeed(Parcel in) {
        id = in.readInt();
        isg = in.readInt();
        user_id = in.readString();
        title = in.readString();
        phone = in.readString();
        name = in.readString();
        image = in.readString();
        prenom = in.readString();
        bname = in.readString();

        des = in.readString();
        durationreq = in.readString();
        typereq = in.readString();
        datereq = in.readString();
        statereq = in.readString();
        adrs = in.readString();
        viewreq = in.readString();
        datereqs = in.readString();
        prix = in.readString();
        url = in.readString();
    }

    public static final Creator<HomeadsFeed> CREATOR = new Creator<HomeadsFeed>() {
        @Override
        public HomeadsFeed createFromParcel(Parcel in) {
            return new HomeadsFeed(in);
        }

        @Override
        public HomeadsFeed[] newArray(int size) {
            return new HomeadsFeed[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(user_id);
        dest.writeString(des);
        dest.writeString(title);
        dest.writeString(phone);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(prenom);
        dest.writeString(bname);

        dest.writeString(des);
        dest.writeString(datereqs);
        dest.writeString(datereq);
        dest.writeString(durationreq);
        dest.writeString(statereq);
        dest.writeString(prix);
        dest.writeString(typereq);
        dest.writeString(adrs);
        dest.writeString(url);
        dest.writeString(viewreq);
    }

*/
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }


    public String getAdrs() {
        return adrs;
    }

    public void setAdrs(String adrs) {
        this.adrs = adrs;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    private int isg=0;
    public String getFullGagner(Context ctx) {
        return isg == 0 ? ctx.getString(R.string.text_pause) : ctx.getString(R.string.text_actif);
    }

    public int getIsg() {
        return isg;
    }

    public void setIsg(int isg) {
        this.isg = isg;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }



    private String phone;
    private  String prenom;
    private String name;
    private String image;

    public String getDaterep() {
        return daterep;
    }

    public void setDaterep(String daterep) {
        this.daterep = daterep;
    }

    public String getType_s() {
        return type_s;
    }

    public void setType_s(String type_s) {
        this.type_s = type_s;
    }

    public String getReq_id() {
        return req_id;
    }

    public void setReq_id(String req_id) {
        this.req_id = req_id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getPrority() {
        return prority;
    }

    public void setPrority(String prority) {
        this.prority = prority;
    }

    public String getDatereps() {
        return datereps;
    }

    public void setDatereps(String datereps) {
        this.datereps = datereps;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    private String bname;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getCategorie() {
        return AppData.TR(type_s);
    }



    public String getFullName() {
        if (bname == null || bname.matches(""))
            return name + " " + prenom;
        return bname;
    }


    public String getPinLink() {
        return NetworkModule.PIN_URL + (image == null ? "" : image);
    }




    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}

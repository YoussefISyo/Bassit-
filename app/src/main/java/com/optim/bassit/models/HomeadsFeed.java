package com.optim.bassit.models;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.optim.bassit.R;
import com.optim.bassit.base.BaseModel;
import com.optim.bassit.data.AppData;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.di.modules.NetworkModule;
import com.optim.bassit.utils.MapHelper;


public  class HomeadsFeed  {




    private int id;
    private  String des;
    private  String datereq;
    private  String typereq;
    private  String type_ar;
    private  String type_en;
    private  String statereq;
    private  String durationreq;
    private  String url;
    private  String viewreq;
    private  String prix;
    private  String datereqs;
    private  String adrs;
    private  String cat;
    private  String dif;
    private  String nego;
    private  String date_now;
    private  int urgence;
    private  String date_start;
    private  String unity;
    private  int quantity;
    private int official_account;
    private int status;

    public String getNego() {
        return nego;
    }

    public void setNego(String ngo) {
        this.nego = ngo;
    }

    public String getDif() {
        return dif;
    }

    public void setDif(String dif) {
        this.dif = dif;
    }

    public String getDate_now() {
        return date_now;
    }

    public void setDate_now(String date_now) {
        this.date_now = date_now;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getType_ar() {
        return type_ar;
    }

    public void setType_ar(String type_ar) {
        this.type_ar = type_ar;
    }

    public String getType_en() {
        return type_en;
    }

    public void setType_en(String type_en) {
        this.type_en = type_en;
    }

    public int getOfficial_account() {
        return official_account;
    }

    public void setOfficial_account(int official_account) {
        this.official_account = official_account;
    }

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

    public String getDatereq() {
        return datereq;
    }

    public void setDatereq(String datereq) {
        this.datereq = datereq;
    }

    public String getTypereq() {
        return AppData.TR(cat);
    }

    public void setTypereq(String typereq) {
        this.typereq = typereq;
    }

    public String getStatereq() {
        return statereq;
    }

    public void setStatereq(String statereq) {
        this.statereq = statereq;
    }

    public String getDurationreq() {
        return durationreq;
    }

    public void setDurationreq(String durationreq) {
        this.durationreq = durationreq;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getViewreq() {
        return viewreq;
    }

    public void setViewreq(String viewreq) {
        this.viewreq = viewreq;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getDatereqs() {
        return datereqs;
    }

    public void setDatereqs(String datereqs) {
        this.datereqs = datereqs;
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

    private String user_id;


    private String title;
    private String phone;
    private  String prenom;
    private String name;
    private String image;






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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategorie() {
        return AppData.TR(typereq);
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


    public int getUrgence() {
        return urgence;
    }

    public void setUrgence(int urgence) {
        this.urgence = urgence;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}

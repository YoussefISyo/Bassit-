package com.optim.bassit.models;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.optim.bassit.R;
import com.optim.bassit.data.AppData;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.di.modules.NetworkModule;
import com.optim.bassit.utils.MapHelper;

public class HomeFeed implements Parcelable {


    private int id;
    private int gagner;
    private int scount;

    public HomeFeed() {

    }

    protected HomeFeed(Parcel in) {
        id = in.readInt();
        gagner = in.readInt();
        scount = in.readInt();
        pause = in.readInt();
        pure = in.readDouble();
        cgagner = in.readInt();
        isg = in.readInt();
        user_id = in.readInt();
        slots = in.readInt();
        photos = in.readInt();
        description = in.readString();
        title = in.readString();
        featured = in.readString();
        phone = in.readString();
        tags = in.readString();
        plan = in.readInt();
        name = in.readString();
        image = in.readString();
        prenom = in.readString();
        therole = in.readInt();
        bcountry = in.readString();
        dist = in.readDouble();
        bcity = in.readString();
        cati = in.readInt();
        sousi = in.readInt();
        bname = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        categorie = in.readString();
        souscategorie = in.readString();
        official_account = in.readInt();
        status = in.readInt();
    }

    public static final Creator<HomeFeed> CREATOR = new Creator<HomeFeed>() {
        @Override
        public HomeFeed createFromParcel(Parcel in) {
            return new HomeFeed(in);
        }

        @Override
        public HomeFeed[] newArray(int size) {
            return new HomeFeed[size];
        }
    };

    public int getScount() {
        return scount;
    }

    public void setScount(int scount) {
        this.scount = scount;
    }

    public int getPause() {
        return pause;
    }

    public void setPause(int pause) {
        this.pause = pause;
    }

    private int pause;

    public double getPure() {
        return pure;
    }

    public void setPure(double pure) {
        this.pure = pure;
    }

    private double pure;
    private int cgagner;
    private String min_price;
    private String avg_rating;
    private String num_rating;
    private int official_account;
    private int status;

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

    public String getNum_rating() {
        return num_rating;
    }

    public void setNum_rating(String num_rating) {
        this.num_rating = num_rating;
    }

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    //private int isg;


    public int getCgagner() {
        return cgagner;
    }

    public void setCgagner(int cgagner) {
        this.cgagner = cgagner;
    }

    private int isg;
    public String getFullGagner(Context ctx) {
        return isg == 0 || gagner == 0? ctx.getString(R.string.text_pause) : ctx.getString(R.string.text_actif);
    }

    public int getIsg() {
        return isg;
    }

    public void setIsg(int isg) {
        this.isg = isg;
    }


    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
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

    private int user_id;

    public int getPhotos() {
        return photos;
    }

    public void setPhotos(int photos) {
        this.photos = photos;
    }

    public int getSlots() {
        if (isg == 1 && cgagner >= gagner)
            return 1;
        return 0;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    private int slots;
    private int photos;
    private String description;
    private String title;
    private String featured;
    private String phone;
    private String tags;
    private int plan;
    private String name;
    private String image;
    private String prenom;
    private int therole;
    private String bcountry;

    public double getDist() {
        return dist;
    }


    public void setDist(double dist) {
        this.dist = dist;
    }

    private double dist;

    public int getTherole() {
        return therole;
    }

    public void setTherole(int therole) {
        this.therole = therole;
    }

    public String getBcity() {
        return bcity;
    }

    public void setBcity(String bcity) {
        this.bcity = bcity;
    }

    private String bcity;


    private int cati;
    private int sousi;

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    private String bname;

    private double lat;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    private double lon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategorie() {
        return AppData.TR(categorie);
    }
    public String getCategorien() {
        return AppData.en(categorie);
    }
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getSouscategorie() {
        return AppData.TR(souscategorie);
    }
    public String getSouscategorien() {
        return AppData.en(souscategorie);
    }
    public void setSouscategorie(String souscategorie) {
        this.souscategorie = souscategorie;
    }

    private String categorie;
    private String souscategorie;

    public String getFullName() {
        if (bname == null || bname.matches(""))
            return name + " " + prenom;
        return bname;
    }


    public String getPinLink() {
        return NetworkModule.PIN_URL + (image == null ? "" : image);
    }

    public String getServiceImageLink(int size) {
        if (featured == null)
            return null;
        return NetworkModule.SERVICE_IMAGE_URL + featured + "/" + size;
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

    public String getIconCat() {
        return NetworkModule.ICONS_URL + "id/" + cati;
    }

    public String getIconSous() {
        return NetworkModule.ICONS_SOUS_URL + "id/" + sousi;
    }

    public int getGagner() {
        return gagner;
    }

    public void setGagner(int gagner) {
        this.gagner = gagner;
    }

    public String getTheDistance(Context ctx) {
        return MapHelper.getInstance().distanceString(String.valueOf(lat), String.valueOf(lon), CurrentUser.getInstance().getmCustomer().getMylat(), CurrentUser.getInstance().getmCustomer().getMylon(), ctx);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(gagner);
        dest.writeInt(scount);
        dest.writeInt(pause);
        dest.writeDouble(pure);
        dest.writeInt(cgagner);
        dest.writeInt(isg);
        dest.writeInt(user_id);
        dest.writeInt(slots);
        dest.writeInt(photos);
        dest.writeString(description);
        dest.writeString(title);
        dest.writeString(featured);
        dest.writeString(phone);
        dest.writeString(tags);
        dest.writeInt(plan);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(prenom);
        dest.writeInt(therole);
        dest.writeString(bcountry);
        dest.writeDouble(dist);
        dest.writeString(bcity);
        dest.writeInt(cati);
        dest.writeInt(sousi);
        dest.writeString(bname);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(categorie);
        dest.writeString(souscategorie);
        dest.writeInt(official_account);
        dest.writeInt(status);
    }
}

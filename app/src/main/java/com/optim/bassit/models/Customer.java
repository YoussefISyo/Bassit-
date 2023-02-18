package com.optim.bassit.models;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.optim.bassit.R;
import com.optim.bassit.di.modules.NetworkModule;

public class Customer implements Parcelable {

    private Integer id;

    protected Customer(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        pause = in.readInt();
        name = in.readString();
        country = in.readString();
        wilaya = in.readString();
        city = in.readString();
        bcountry = in.readString();
        tags = in.readString();
        bwilaya = in.readString();
        bcity = in.readString();
        gcount = in.readInt();
        plan = in.readInt();
        gagner = in.readInt();
        points = in.readInt();
        bonus = in.readInt();
        credit = in.readInt();
        access_token = in.readString();
        bname = in.readString();
        maplevel = in.readInt();
        prenom = in.readString();
        cat = in.readString();
        therole = in.readInt();
        address = in.readString();
        description = in.readString();
        token = in.readString();
        phone = in.readString();
        ccp = in.readString();
        nomprenomccp = in.readString();
        email = in.readString();
        image = in.readString();
        password = in.readString();
        selected = in.readByte() != 0;
        isentreprise = in.readInt();
        lat = in.readString();
        lon = in.readString();
        mylat = in.readString();
        mylon = in.readString();
        old = in.readString();
       // invitecode = in.readString();
        pwd = in.readString();
        pwd2 = in.readString();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeInt(pause);
        dest.writeString(name);
        dest.writeString(country);
        dest.writeString(wilaya);
        dest.writeString(city);
        dest.writeString(bcountry);
        dest.writeString(tags);
        dest.writeString(bwilaya);
        dest.writeString(bcity);
        dest.writeInt(gcount);
        dest.writeInt(plan);
        dest.writeInt(gagner);
        dest.writeInt(points);
        dest.writeInt(bonus);
        dest.writeInt(credit);
        dest.writeString(access_token);
        dest.writeString(bname);
        dest.writeInt(maplevel);
        dest.writeString(prenom);
        dest.writeString(cat);
        dest.writeInt(therole);
        dest.writeString(address);
        dest.writeString(description);
        dest.writeString(token);
        dest.writeString(phone);
        dest.writeString(ccp);
        dest.writeString(nomprenomccp);
        dest.writeString(email);
        dest.writeString(image);
        dest.writeString(password);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeInt(isentreprise);
        dest.writeString(lat);
        dest.writeString(lon);
        dest.writeString(mylat);
        dest.writeString(mylon);
        dest.writeString(old);
        dest.writeString(pwd);
        dest.writeString(pwd2);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public int getPause() {
        return pause;
    }

    public void setPause(int pause) {
        this.pause = pause;
    }

    private int pause;

    private String name;


    public String getNomprenomccp() {
        return nomprenomccp;
    }

    public void setNomprenomccp(String nomprenomccp) {
        this.nomprenomccp = nomprenomccp;
    }


    public String getCcp() {
        return ccp;
    }

    public void setCcp(String ccp) {
        this.ccp = ccp;
    }

    private String country;
    private String wilaya;
    private String city;

    private String bcountry;
    private String tags;
    private String bwilaya;
    private String bcity;

    private int official_account;
    private int rate_us;
    private int share_app;
    private int like_fb;
    private int like_insta;
    private int status;

    public int getRate_us() {
        return rate_us;
    }

    public void setRate_us(int rate_us) {
        this.rate_us = rate_us;
    }

    public int getShare_app() {
        return share_app;
    }

    public void setShare_app(int share_app) {
        this.share_app = share_app;
    }

    public int getLike_fb() {
        return like_fb;
    }

    public void setLike_fb(int like_fb) {
        this.like_fb = like_fb;
    }

    public int getLike_insta() {
        return like_insta;
    }

    public void setLike_insta(int like_insta) {
        this.like_insta = like_insta;
    }

    public int getGcount() {
        return gcount;
    }

    public void setGcount(int gcount) {
        this.gcount = gcount;
    }

    private int gcount;


    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    private int plan;
    private int gagner;

    public String getFullPoints(Context ctx) {
        return points + " "+ctx.getString(R.string.text_pts) ;
    }


    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    private int points;
    private int bonus;

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getGagner() {
        return gagner;
    }

    public void setGagner(int gagner) {
        this.gagner = gagner;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    private int credit;


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    private String access_token;


    public String getBcountry() {
        return bcountry;
    }

    public void setBcountry(String bcountry) {
        this.bcountry = bcountry;
    }

    public String getBwilaya() {
        return bwilaya;
    }

    public void setBwilaya(String bwilaya) {
        this.bwilaya = bwilaya;
    }

    public String getBcity() {
        return bcity;
    }

    public void setBcity(String bcity) {
        this.bcity = bcity;
    }

    public String getBname() {
        return bname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public int getOfficial_account() {
        return official_account;
    }

    public void setOfficial_account(int official_account) {
        this.official_account = official_account;
    }

    private String bname;


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWilaya() {
        return wilaya;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }

    public String getLat() {
        return lat == null || lat.matches("") ? "0" : lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon == null || lon.matches("") ? "0" : lon;

    }

    public boolean hasLonLat() {
        return !(getLat().equals("0") && getLon().equals("0"));
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getMylat() {
        return mylat;
    }


    public void setMylat(String mylat) {
        this.mylat = mylat;
    }

    public String getMylon() {
        return mylon;
    }

    public void setMylon(String mylon) {
        this.mylon = mylon;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public int getMaplevel() {
        return maplevel;
    }

    public void setMaplevel(int maplevel) {
        this.maplevel = maplevel;
    }

    private int maplevel;
    private String prenom;
    private String cat;


    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getFullName() {
        if (bname == null || bname.matches(""))
            return name + " " + prenom;
        return bname;
    }


    public int getTherole() {
        return therole;
    }

    public void setTherole(int therole) {
        this.therole = therole;
    }

    private int therole;
    private String address;
    private String description;
    private String token;
    private String phone;
    private String ccp;
    private String nomprenomccp;
    private String email;
    private String image;
    private String password;
    private boolean selected;
    private int isentreprise;
    private String lat;
    private String lon;

    private String mylat;
    private String mylon;


    public Customer(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Customer() {

    }

    public Customer(String email) {
        this.email = email;
    }
    String invitecode;
    String Voffers; String Vrep;

    public String getVoffers() {
        return Voffers;
    }

    public void setVoffers(String voffers) {
        Voffers = voffers;
    }

    public String getVrep() {
        return Vrep;
    }

    public void setVrep(String vrep) {
        Vrep = vrep;
    }

    String macadress;
    public Customer(String name, String prenom, String phone, String email, String password,String invitecode,String macadress) {
        this.name = name;
        this.prenom = prenom;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.invitecode = invitecode;
        this.macadress = macadress;
    }

    public Customer(String name, String prenom, String phone, String address) {
        this.name = name;
        this.prenom = prenom;
        this.phone = phone;
        this.address = address;
    }

    public Customer(int id, String name, String prenom, String description, String phone, String address, int therole) {
        this.id = id;
        this.name = name;
        this.prenom = prenom;
        this.description = description;
        this.phone = phone;
        this.address = address;
        this.therole = therole;
    }

    public String getPinLink() {
        return NetworkModule.PIN_URL + (image == null ? "" : image) ;
    }

    public Customer(String description, String tel, String address) {

        this.description = description;
        this.phone = tel;
        this.address = address;
    }

    public void setIsEntreprise(int isentreprise) {
        this.isentreprise = isentreprise;
    }

    public Integer getId() {
        return id;
    }

    public String getUserName() {
        if (name == null || name.matches("") || prenom == null || prenom.matches(""))
            return "!";

        return name + " " + prenom;

    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }




    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getGPSAddress() {
        String s = "";
        if (getCountry() != null && !getCountry().matches(""))
            s += getCountry() + " > ";
        if (getWilaya() != null && !getWilaya().matches(""))
            s += getWilaya() + " > ";
        if (getCity() != null && !getCity().matches(""))
            s += getCity();
        return s;
    }

    public String getBGPSAddress() {
        String s = "";
        if (getBcountry() != null && !getBcountry().matches(""))
            s += getBcountry() + " > ";
        if (getBwilaya() != null && !getBwilaya().matches(""))
            s += getBwilaya() + " > ";
        if (getBcity() != null && !getBcity().matches(""))
            s += getBcity();
        return s;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void fillTempPWD(String old, String pwd, String pwd2) {
        this.old = old;
        this.pwd = pwd;
        this.pwd2 = pwd2;
    }

    private String old;
    private String pwd;
    private String pwd2;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

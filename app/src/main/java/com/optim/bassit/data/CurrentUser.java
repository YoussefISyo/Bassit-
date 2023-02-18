package com.optim.bassit.data;

import com.optim.bassit.models.Customer;

public class CurrentUser {
    private static CurrentUser INSTANCE;


    private Customer mCustomer;

    private String Country;
    private String Wilaya;
    private String City;


    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getWilaya() {
        return Wilaya;
    }

    public void setWilaya(String wilaya) {
        Wilaya = wilaya;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }


    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    private String lat;
    private String lon;


    public String getApitoken() {
        return apitoken;
    }

    public void setApitoken(String apitoken) {
        this.apitoken = apitoken;
    }

    private String apitoken;

    private CurrentUser() {
    }

    public synchronized static CurrentUser getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CurrentUser();
        }
        return INSTANCE;
    }

    public Customer getmCustomer() {
        return mCustomer;
    }

    public void setmCustomer(Customer mCustomer) {
        this.mCustomer = mCustomer;
    }


    public String getAvatar() {
        return mCustomer.getPinLink();
    }

    public boolean isLogin() {
        return getmCustomer() != null;
    }

    public boolean isPro() {
        return isLogin() && (getmCustomer().getTherole() == 2 || getmCustomer().getTherole() == 33);
    }


    public String getFullAddress() {
        String s = "";
        if (getCountry() != null && !getCountry().matches(""))
            s += getCountry() + " > ";
        if (getWilaya() != null && !getWilaya().matches(""))
            s += getWilaya() + " > ";
        if (getCity() != null && !getCity().matches(""))
            s += getCity();
        return s;
    }

    public void logout() {

        INSTANCE = null;
    }
}
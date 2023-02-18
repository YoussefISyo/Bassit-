package com.optim.bassit.models;


import com.optim.bassit.base.BaseModel;
import com.optim.bassit.di.modules.NetworkModule;

public class Ads extends BaseModel {

    int id;
    String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getPriorite() {
        return priorite;
    }

    public void setPriorite(int priorite) {
        this.priorite = priorite;
    }

    public String getFullImage() {
        return NetworkModule.ADS_URL + image;
    }

    int type;
    String target;
    int etat;
    int place;
    int priorite;

}

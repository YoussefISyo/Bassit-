package com.optim.bassit.models;

import com.optim.bassit.base.BaseModel;
import com.optim.bassit.di.modules.NetworkModule;
import com.optim.bassit.utils.OptimTools;

import java.util.Date;

public class Avis extends BaseModel {

    private int id;
    private int client_id;
    private int service_id;
    private float avis;
    private String comment;
    private String closed_at;
    private String name;
    private String prenom;
    private String bname;
    private String image;

    private float client_avis;

    public float getClient_avis() {
        return client_avis;
    }

    public void setClient_avis(float client_avis) {
        this.client_avis = client_avis;
    }

    public String getClient_comment() {
        return client_comment;
    }

    public void setClient_comment(String client_comment) {
        this.client_comment = client_comment;
    }

    private String client_comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setClosed_at(String closed_at) {
        this.closed_at = closed_at;
    }

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

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFullName() {
        if (bname == null || bname.matches(""))
            return name + " " + prenom;
        return bname;
    }

    public String getPinLink() {
        return NetworkModule.PIN_URL + (image == null ? "" : image);
    }


    public String getClosed_at() {
        try {
            Date a = OptimTools.toDateTime(closed_at);
            String b = OptimTools.dateToFRString(a);
            return b;
        } catch (Exception ex) {
            return closed_at;
        }
    }

    public float getAvis() {
        return avis;
    }

    public void setAvis(float avis) {
        this.avis = avis;
    }
}

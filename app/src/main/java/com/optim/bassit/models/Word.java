package com.optim.bassit.models;


import com.optim.bassit.base.BaseModel;

public class Word extends BaseModel {


    private String designation;
    private String d_ar;
    private String d_en;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getD_ar() {
        return d_ar;
    }

    public void setD_ar(String d_ar) {
        this.d_ar = d_ar;
    }

    public String getD_en() {
        return d_en;
    }

    public void setD_en(String d_en) {
        this.d_en = d_en;
    }
}
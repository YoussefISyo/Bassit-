package com.optim.bassit.models;

import com.optim.bassit.base.BaseModel;

public class Stats  extends BaseModel {

    private Integer id;
    private String mois;
    private int annee;

    private double depenses;
    private double benefice;
    private double services;
    private double credit;

    public double getCredit() {
        return credit;
    }

    public double getServices() {
        return services;
    }

    public double getBenefice() {
        return benefice;
    }

    public double getDepenses() {
        return depenses;
    }

    public Integer getId() {
        return id;
    }

    public String getMois() {
        return mois;
    }

    public int getAnnee() {
        return annee;
    }
}

package com.optim.bassit.models;

import com.optim.bassit.base.BaseModel;

public class Montant  extends BaseModel {

    private Integer id;
    private String type;
    private String designation;
    private String created_at;
    private String date;
    private double montant;

    public Montant(String type, Double montant, String date, String designation) {
        this.type = type;
        this.montant = montant;
        this.date = date;
        this.designation = designation;
    }

    public Montant() {

    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getDesignation() {
        return designation;
    }

    public double getMontant() {
        return montant;
    }
}

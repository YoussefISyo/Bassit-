package com.optim.bassit.models;

public class Activite {

    private Integer id;
    private String client;
    private String date;
    private double montant;
    private double charge;
    private double payed;


    public String getDate() {
        return date;
    }


    public Integer getId() {
        return id;
    }

    public double getCharge() {
        return charge;
    }

    public double getPayed() {
        return payed;
    }



    public String getClient() {
        return client;
    }

    public double getMontant() {
        return montant;
    }
}

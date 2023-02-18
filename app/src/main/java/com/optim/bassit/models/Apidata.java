package com.optim.bassit.models;

import java.util.List;

public class Apidata {
    private String dif;
    private String message;
    private String date_now;
    public List<HomeadsFeed> getData() {
        return data;
    }

    private List<HomeadsFeed> data;


    public String getDif() {
        return dif;
    }

    public String getDate_now() {
        return date_now;
    }

    public String getMessage() {
        return message;
    }
}

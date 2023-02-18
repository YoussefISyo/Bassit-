package com.optim.bassit.models;

import java.util.List;

public class Apidatahome {
    private String notifi;
    public List<HomeFeed> getData() {
        return data;
    }

    private List<HomeFeed> data;

    public String getNotifi() {
        return notifi;
    }

    public void setNotifi(String notifi) {
        this.notifi = notifi;
    }
}

package com.optim.bassit.models;

import com.optim.bassit.base.BaseModel;

public class Compte  extends BaseModel {

    private int id;

    private String title;
    private String content;

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }
}

package com.optim.bassit.models;

import com.optim.bassit.base.BaseModel;
import com.optim.bassit.data.AppData;
import com.optim.bassit.di.modules.NetworkModule;


public class SousCategorie extends BaseModel {
    private int id;
    private String designation;
    private String categorie;
    private String image;
    private int categorie_id;
    private int selected;

    public String getDesignation() {
        return AppData.TR(designation);
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getIcon() {
        return NetworkModule.ICONS_SOUS_URL + image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategorie_id() {
        return categorie_id;
    }

    public void setCategorie_id(int categorie_id) {
        this.categorie_id = categorie_id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}

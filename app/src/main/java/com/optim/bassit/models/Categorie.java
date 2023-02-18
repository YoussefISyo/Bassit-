package com.optim.bassit.models;


import com.optim.bassit.base.BaseModel;
import com.optim.bassit.data.AppData;
import com.optim.bassit.di.modules.NetworkModule;

public class Categorie extends BaseModel {
    public String getCat() {
        return AppData.TR(cat);
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getSous() {
        return AppData.TR(sous);
    }

    public void setSous(String sous) {
        this.sous = sous;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public int getSous_id() {
        return sous_id;
    }

    public void setSous_id(int sous_id) {
        this.sous_id = sous_id;
    }

    private String cat;
    private String sous;
    private String image;

    private int cat_id;

    public int getHas() {
        return has;
    }

    public void setHas(int has) {
        this.has = has;
    }

    private int has;
    private int sous_id;


    private int id;
    private String name;
    private int selected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return AppData.TR(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return NetworkModule.ICONS_URL + image;
    }


    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

package com.optim.bassit.models;


import android.content.Context;

import com.optim.bassit.R;
import com.optim.bassit.data.AppData;
import com.optim.bassit.di.modules.NetworkModule;


public  class notifi {



    private int id;
    private  String des;
    private  String date_created;
    private  String opr_id;
    private  String user_id;
    private  String opr_type;
    private  String title;
    private  String to_who;
    private  String st;
    private  String vu;

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getOpr_id() {
        return opr_id;
    }

    public void setOpr_id(String opr_id) {
        this.opr_id = opr_id;
    }

    public String getOpr_type() {
        return opr_type;
    }

    public void setOpr_type(String opr_type) {
        this.opr_type = opr_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTo_who() {
        return to_who;
    }

    public void setTo_who(String to_who) {
        this.to_who = to_who;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getVu() {
        return vu;
    }

    public void setVu(String vu) {
        this.vu = vu;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }



    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    private int isg=0;
    public String getFullGagner(Context ctx) {
        return isg == 0 ? ctx.getString(R.string.text_pause) : ctx.getString(R.string.text_actif);
    }

    public int getIsg() {
        return isg;
    }

    public void setIsg(int isg) {
        this.isg = isg;
    }








    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }



    private String phone;
    private  String prenom;
    private String name;
    private String image;





    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    private String bname;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }






    public String getFullName() {
        if (bname == null || bname.matches(""))
            return name + " " + prenom;
        return bname;
    }


    public String getPinLink() {
        return NetworkModule.PIN_URL + (image == null ? "" : image);
    }




    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }





}

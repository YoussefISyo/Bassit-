package com.optim.bassit.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.di.modules.NetworkModule;
import com.optim.bassit.utils.OptimTools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Chat implements Parcelable {

    public Chat() {

    }


    private int id;
    private String clientname;
    private String servicename;
    private int user_service_id;
    private int type;
    private String message;
    private String stamp;
    private int isclient;
    private int service_id;



    public int getIsme() {
        return isme;
    }

    public void setIsme(int isme) {
        this.isme = isme;
    }

    private int isme;

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    private int client_id;

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getBon_id() {
        return bon_id;
    }

    public void setBon_id(int bon_id) {
        this.bon_id = bon_id;
    }

    private int bon_id;

    public String getChk() {
        return chk;
    }

    public void setChk(String chk) {
        this.chk = chk;
    }

    private String chk;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public int getUser_service_id() {
        return user_service_id;
    }

    public void setUser_service_id(int user_service_id) {
        this.user_service_id = user_service_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public int getIsclient() {
        return isclient;
    }


    protected Chat(Parcel in) {
        id = in.readInt();
        clientname = in.readString();
        servicename = in.readString();
        user_service_id = in.readInt();
        type = in.readInt();
        message = in.readString();
        stamp = in.readString();
        isclient = in.readInt();
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    public String getStampHumain() {
        Date date = OptimTools.toDateTime(stamp);
        if (date == null)
            return null;

        if (OptimTools.isToday(date)) {
            try {
                String dstring = new SimpleDateFormat("HH:mm").format(date);
                return dstring;
            } catch (Exception ex) {

            }
        }
        try {
            String dstring = new SimpleDateFormat("dd/MM/yyyy à HH:mm").format(date);
            return dstring;
        } catch (Exception ex) {

        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(clientname);
        parcel.writeString(servicename);
        parcel.writeInt(user_service_id);
        parcel.writeInt(type);
        parcel.writeString(message);
        parcel.writeString(stamp);
        parcel.writeInt(isclient);
    }

    public String getChatImage() {
        if (type == 1) {
            String token = CurrentUser.getInstance().getApitoken();
            return NetworkModule.CHAT_URL + message + "?access_token=" + token;
        }
        return "";
    }
}


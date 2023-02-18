package com.optim.bassit.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.optim.bassit.data.AppData;
import com.optim.bassit.di.modules.NetworkModule;
import com.optim.bassit.utils.OptimTools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tache implements Parcelable {

    private Integer bon_id;


    public int getSplan() {
        return splan;
    }

    public void setSplan(int splan) {
        this.splan = splan;
    }

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }
    String typec;
    String titlec;
    String offer_id;

    public String getTitlec() {
        return titlec;
    }

    public void setTitlec(String titlec) {
        this.titlec = titlec;
    }
    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getTypec() {
        return typec;
    }

    public void setTypec(String type) {
        this.typec = type;
    }

    protected Tache(Parcel in) {
        if (in.readByte() == 0) {
            bon_id = null;
        } else {
            bon_id = in.readInt();
        }
        link_id = in.readInt();
        clientname = in.readString();
       // typec = in.readString();
     // titlec = in.readString();
      //  offer_id = in.readString();
        servicename = in.readString();
        client_id = in.readInt();
        user_service_id = in.readInt();
        splan = in.readInt();
        plan = in.readInt();
        clientbname = in.readString();
        servicebname = in.readString();
        serviceimage = in.readString();
        clientimage = in.readString();
        montant = in.readString();
        client_comment = in.readString();
        if (in.readByte() == 0) {
            client_avis = null;
        } else {
            client_avis = in.readFloat();
        }
        categorie = in.readString();
        title = in.readString();
        souscategorie = in.readString();
        status = in.readInt();
        service_id = in.readInt();
        stamp = in.readString();
        bseen = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (bon_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(bon_id);
        }
        dest.writeInt(link_id);
        dest.writeString(clientname);
        dest.writeString(servicename);
        dest.writeInt(client_id);
        dest.writeInt(user_service_id);
        dest.writeInt(splan);
        dest.writeInt(plan);
        dest.writeString(clientbname);
     //   dest.writeString(typec);
     //   dest.writeString(offer_id);
     //  dest.writeString(titlec);
        dest.writeString(servicebname);
        dest.writeString(serviceimage);
        dest.writeString(clientimage);
        dest.writeString(montant);
        dest.writeString(client_comment);
        if (client_avis == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(client_avis);
        }
        dest.writeString(categorie);
        dest.writeString(title);
        dest.writeString(souscategorie);
        dest.writeInt(status);
        dest.writeInt(service_id);
        dest.writeString(stamp);
        dest.writeInt(bseen);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Tache> CREATOR = new Creator<Tache>() {
        @Override
        public Tache createFromParcel(Parcel in) {
            return new Tache(in);
        }

        @Override
        public Tache[] newArray(int size) {
            return new Tache[size];
        }
    };

    public int getLink_id() {
        return link_id;
    }

    public void setLink_id(int link_id) {
        this.link_id = link_id;
    }

    private int link_id;
    private String clientname;
    private String servicename;
    private int client_id;
    private int user_service_id;

    private int splan;
    private int plan;

    private String clientbname;
    private String servicebname;

    private String serviceimage;
    private String clientimage;


    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    private String montant;

    private String client_comment;

    public String getClient_comment() {
        return client_comment;
    }

    public void setClient_comment(String client_comment) {
        this.client_comment = client_comment;
    }

    public Float getClient_avis() {
        return client_avis;
    }

    public void setClient_avis(Float client_avis) {
        this.client_avis = client_avis;
    }

    private Float client_avis;


    public String getFullClientBName() {
        if (clientbname == null || clientbname.matches(""))
            return clientname;
        return clientbname;
    }

    public String getFullServiceBName() {
        if (servicebname == null || servicebname.matches(""))
            return servicename;
        return servicebname;
    }

    public String getClientbname() {
        return clientbname;
    }

    public void setClientbname(String clientbname) {
        this.clientbname = clientbname;
    }

    public String getServicebname() {
        return servicebname;
    }

    public void setServicebname(String servicebname) {
        this.servicebname = servicebname;
    }


    public String getCategorie() {
        return AppData.TR(categorie) ;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getSouscategorie() {
        return AppData.TR(souscategorie);
    }

    public void setSouscategorie(String souscategorie) {
        this.souscategorie = souscategorie;
    }

    private String categorie;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
    private String souscategorie;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    private int service_id;

    public int getBon_id() {
        return bon_id;
    }

    public void setBon_id(int bon_id) {
        this.bon_id = bon_id;
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

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getUser_service_id() {
        return user_service_id;
    }

    public void setUser_service_id(int user_service_id) {
        this.user_service_id = user_service_id;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

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
            String dstring = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
            return dstring;
        } catch (Exception ex) {

        }
        return null;
    }

    public int getBseen() {
        return bseen;
    }

    public void setBseen(int bseen) {
        this.bseen = bseen;
    }

    private String stamp;
    private int bseen;


    public String getClientPinLink() {
        String ig = NetworkModule.PIN_URL + (clientimage == null ? "" : clientimage);
        return ig;
    }

    public String getServicePinLink() {
        String ig = NetworkModule.PIN_URL + (serviceimage == null ? "" : serviceimage);
        return ig;
    }
}

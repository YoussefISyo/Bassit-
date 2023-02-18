package com.optim.bassit.models;

import android.graphics.Bitmap;

import com.optim.bassit.base.BaseModel;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Media  extends BaseModel {

    private int id;
    private String link;
    private Bitmap bitmap;

    private Service service;
    private File file;
    private RequestBody requestBody;
    private MultipartBody.Part part;

    public String getLink() {
        return link;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public File getFile() {
        return file;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public MultipartBody.Part getPart() {
        return part;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public void setPart(MultipartBody.Part part) {
        this.part = part;
    }

    public Media(Bitmap bitmap, File file, RequestBody requestBody, MultipartBody.Part part){
        this.bitmap=bitmap;
        this.file=file;
        this.requestBody=requestBody;
        this.part =part;
    }

    public Media( MultipartBody.Part part){
        this.part =part;
    }
    public Media(){}
}

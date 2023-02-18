package com.optim.bassit.models;

public class ApiResponse {
    private boolean error;
    private String message;
    private String id;
    public String getData() {
        return data;
    }

    private String data;

    public String getId() {
        return id;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}

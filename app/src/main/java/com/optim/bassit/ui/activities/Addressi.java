package com.optim.bassit.ui.activities;

public class Addressi {
    private String AdminArea;
    private String Locality;
    private String CountryCode;
    private String CountryName="Algeria";

    public Addressi() {
    }

    public String getAdminArea() {
        return AdminArea;
    }

    public void setAdminArea(String adminArea) {
        AdminArea = adminArea;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }
}

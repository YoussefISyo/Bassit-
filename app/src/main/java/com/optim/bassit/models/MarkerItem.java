package com.optim.bassit.models;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerItem implements ClusterItem {
private String title;
private String snippet;
private LatLng latLng;
    private Marker marker;
private BitmapDescriptor icon;
    private Customer customer;
public MarkerItem(MarkerOptions markerOptions, Customer customer, Marker marker) {
    this.latLng = markerOptions.getPosition();
    this.title = markerOptions.getTitle();
    this.snippet = markerOptions.getSnippet();
    this.icon = markerOptions.getIcon();
    this.customer=customer;
    this.marker=marker;
}

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
public LatLng getPosition() {
    return latLng;
}

public String getTitle() {
    return title;
}

public String getSnippet() {
    return snippet;
}

public void setLatLng(LatLng latLng) {
    this.latLng = latLng;
}

public BitmapDescriptor getIcon() {
    return icon;
}

public void setIcon(BitmapDescriptor icon) {
    this.icon = icon;
}

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
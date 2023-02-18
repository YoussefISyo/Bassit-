package com.optim.bassit.ui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseFragment;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Customer;
import com.optim.bassit.ui.activities.ProfileActivity;
import com.optim.bassit.ui.adapters.InfoWindowAdapter;
import com.optim.bassit.utils.MapHelper;
import com.optim.bassit.utils.OptimTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    public MapFragment() {
    }


    @Inject
    AppApi appApi;
    List<Marker> all;


    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vwM = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        return vwM;

    }

    Handler handler;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.isMyLocationEnabled();
        mMap.clear();
        LatLng ll = new LatLng(Double.valueOf(CurrentUser.getInstance().getmCustomer().getMylat()), Double.valueOf(CurrentUser.getInstance().getmCustomer().getMylon()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 14));


        handler = new Handler();

        loadPins(ll, 20, 14, 100);

        googleMap.setOnCameraChangeListener(cameraPosition -> {


            LatLngBounds bnds = mMap.getProjection().getVisibleRegion().latLngBounds;
            double dist = MapHelper.getInstance().calculateDistance(bnds.northeast, bnds.southwest);


            loadPins(cameraPosition.target, dist, cameraPosition.zoom, 2000);


        });


        mMap.setOnInfoWindowClickListener(this);
    }

    private void handleZoom(double zoom) {
        if (all != null)
            for (Marker m : all) {
                Customer cus = (Customer) m.getTag();
                if (cus == null)
                    m.setVisible(zoom > 10);
                else
                    m.setVisible(zoom > cus.getMaplevel());
                //8 here is your zoom level, you can set it as your need.
            }
    }

    private void loadPins(LatLng ll, double dist, double zoom, int delay) {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(() -> appApi.getMap(String.valueOf(ll.latitude), String.valueOf(ll.longitude), String.valueOf(zoom), String.valueOf(dist)).enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccessful()) {
                    handleZoom(zoom);
                    for (Customer customer : response.body()) {

                        loadImageAndPutMarker(customer);

                    }

                    handleZoom(zoom);

                }
            }


            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {

            }
        }), delay);
    }


    private void loadImageAndPutMarker(Customer customer) {

        if (all != null)
            for (Marker mm : all) {
                if (mm.getTag() == null)
                    continue;
                if (((Customer) mm.getTag()).getId() == customer.getId())
                    return;
            }
        int wh = (int) OptimTools.dpToPixels(35, getActivity());
        Thread thread = new Thread(() -> {


            Bitmap bmp2 = null;
            try {
                bmp2 = OptimTools.getPicasso(customer.getPinLink() + "/" + customer.getPlan()).get();

            } catch (IOException e) {
                return;
            }

            Bitmap bmp = Bitmap.createScaledBitmap(bmp2, wh, wh, false);

            getActivity().runOnUiThread(() -> {
                InfoWindowAdapter customInfoWindow = new InfoWindowAdapter(getContext());
                mMap.setInfoWindowAdapter(customInfoWindow);
                MarkerOptions mo = new MarkerOptions().position(new LatLng(Double.valueOf(customer.getLat()), Double.valueOf(customer.getLon()))).title(customer.getFullName());
                if (bmp != null)
                    mo = mo.icon(BitmapDescriptorFactory.fromBitmap(bmp));
                Marker marker = mMap.addMarker(mo);
                marker.setTag(customer);
                if (all == null) {
                    all = new ArrayList<>();
                }
                all.add(marker);

            });
        });
        thread.start();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Customer customer = (Customer) marker.getTag();
        Intent myintent = new Intent(getActivity(), ProfileActivity.class);
        myintent.putExtra("user_id", customer.getId());
        startActivity(myintent);
    }
}
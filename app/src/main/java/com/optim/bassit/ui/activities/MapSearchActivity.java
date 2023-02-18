package com.optim.bassit.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Customer;
import com.optim.bassit.utils.MapHelper;
import com.optim.bassit.utils.OptimTools;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapSearchActivity extends BaseActivity implements OnMapReadyCallback {

    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.t_address)
    TextView t_address;
    @BindView(R.id.b_gps)
    Button b_gps;
    @BindView(R.id.b_save)
    Button b_save;


    @Inject
    AppApi appApi;

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        ButterKnife.bind(this);


        App.getApp(getApplication()).getComponent().inject(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);


        if (getIntent().hasExtra("user") && getIntent().getBooleanExtra("user", false))
            buser = true;
    }

    boolean buser = false;
    Marker marker;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            Toast.makeText(this, R.string.error_geo_coding, Toast.LENGTH_LONG).show();
        }
        try {

            if (!buser) {

                if (CurrentUser.getInstance().getmCustomer() != null && CurrentUser.getInstance().getmCustomer().hasLonLat()) {
                    LatLng loc = new LatLng(Double.valueOf(CurrentUser.getInstance().getmCustomer().getLat()), Double.valueOf(CurrentUser.getInstance().getmCustomer().getLon()));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));
                    marker = map.addMarker(new MarkerOptions().position(loc));
                    setAddress();

                } else
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(CurrentUser.getInstance().getmCustomer().getMylat()), Double.valueOf(CurrentUser.getInstance().getmCustomer().getMylon())), 14));


            } else {
                LatLng loc = new LatLng(Double.valueOf(CurrentUser.getInstance().getmCustomer().getMylat()), Double.valueOf(CurrentUser.getInstance().getmCustomer().getMylon()));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));
                marker = map.addMarker(new MarkerOptions().position(loc));
                setAddress();
            }


        } catch (Exception ex) {

        }
        map.setOnMapClickListener(latLng -> {
            if (marker == null) {
                marker = map.addMarker(new MarkerOptions().position(latLng));
            } else
                marker.setPosition(latLng);
            setAddress();
        });

    }

    @OnClick(R.id.b_gps)
    public void gpsClick() {
        try {

            MapHelper.getInstance().getGPS(this, (myLocation) -> {
                if (myLocation == null)
                    return;
                LatLng myLatLng = new LatLng(myLocation.latitude, myLocation.longitude);
                marker.setPosition(myLatLng);
                setAddress();
            });

        } catch (Exception ex) {
            Alert(getString(R.string.error_position));
        }
    }


    @OnClick(R.id.b_save)
    public void saveClick() {
        if (marker == null) {
            Alert(R.string.please_click_on_the_map);
            return;
        }

        //  Address addr = MapHelper.getInstance().getAddressFromLocation(this, marker.getPosition());


      /*  if (addr == null) {
            Alert(R.string.error_geo_coding);
            return;
        }
*/

        String address = t_address.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("longitude", marker.getPosition().longitude);
        intent.putExtra("latitude", marker.getPosition().latitude);
        intent.putExtra("address", address);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    private void setAddress() {
        Address addr = MapHelper.getInstance().getAddressFromLocation(this, marker.getPosition());
        if (addr == null) {
            t_address.setText("Erreur geocoding");
            return;
        }
        String a = addr.getCountryName();
        String b = addr.getAdminArea();
        String c = addr.getLocality();
        a += " > ";
        if (b == null || b.matches(""))
            a += c;
        else if (c == null || c.matches(""))
            a += b;
        else
            a += b + " > " + c;
        t_address.setText(a);


    }

    @OnClick(R.id.b_back)
    public void goBack() {
        finish();
    }
}
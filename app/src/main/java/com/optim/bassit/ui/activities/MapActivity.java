package com.optim.bassit.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

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

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

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
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);


        App.getApp(getApplication()).getComponent().inject(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);


        if (getIntent().hasExtra("user") && getIntent().getBooleanExtra("user", false))
            buser = true;

        if (getIntent().hasExtra("new") && getIntent().getBooleanExtra("new", false))
            newUser = true;
        if (getIntent().hasExtra("updateInfo") && getIntent().getBooleanExtra("updateInfo", false))
            updateInfo = true;
    }

    boolean buser = false;
    boolean newUser = false;
    boolean updateInfo = false;
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

            if (updateInfo){
                LatLng loc = new LatLng(Double.valueOf(CurrentUser.getInstance().getmCustomer().getLat()), Double.valueOf(CurrentUser.getInstance().getmCustomer().getLon()));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));
                marker = map.addMarker(new MarkerOptions().position(loc));
                setAddress();
            }
            else if (!buser) {

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
        Addressi addr=new Addressi();
        show();

      /*  if (addr == null) {
            Alert(R.string.error_geo_coding);
            return;
        }
*/
        if (updateInfo){
            Intent returnIntent = new Intent();
            returnIntent.putExtra("longitude", String.valueOf(marker.getPosition().longitude));
            returnIntent.putExtra("latitude", String.valueOf(marker.getPosition().latitude));
            returnIntent.putExtra("address", t_address.getText().toString());
            setResult(RESULT_OK, returnIntent);
            finish();
        }
        else if (!newUser){
            if (buser) {

                CurrentUser.getInstance().setCountry(addr.getCountryName());
                CurrentUser.getInstance().setWilaya(addr.getAdminArea());
                CurrentUser.getInstance().setCity(addr.getLocality());

                CurrentUser.getInstance().setLon(String.valueOf(marker.getPosition().longitude));
                CurrentUser.getInstance().setLat(String.valueOf(marker.getPosition().latitude));

                CurrentUser.getInstance().getmCustomer().setCountry(addr.getCountryName());
                CurrentUser.getInstance().getmCustomer().setWilaya(addr.getAdminArea());
                CurrentUser.getInstance().getmCustomer().setCity(addr.getLocality());

                CurrentUser.getInstance().getmCustomer().setMylon(String.valueOf(marker.getPosition().longitude));
                CurrentUser.getInstance().getmCustomer().setMylat(String.valueOf(marker.getPosition().latitude));

                Customer cus2 = new Customer();
                cus2.setCountry(addr.getCountryName());
                cus2.setWilaya(addr.getAdminArea());
                cus2.setCity(addr.getLocality());
                cus2.setMylat(OptimTools.toApiString(marker.getPosition().latitude));
                cus2.setMylon(OptimTools.toApiString(marker.getPosition().longitude));


                fullyHandleResponse(appApi.updatePosition(cus2), new handleResponse() {
                    @Override
                    public void onSuccess() {
                        finish();
                    }

                    @Override
                    public void onFail(String error) {
                        hide();
                    }
                });
            } else {

                CurrentUser.getInstance().getmCustomer().setBcountry(addr.getCountryName());
                CurrentUser.getInstance().getmCustomer().setBwilaya(addr.getAdminArea());
                CurrentUser.getInstance().getmCustomer().setBcity(addr.getLocality());

                CurrentUser.getInstance().getmCustomer().setLon(String.valueOf(marker.getPosition().longitude));
                CurrentUser.getInstance().getmCustomer().setLat(String.valueOf(marker.getPosition().latitude));

                fullyHandleResponse(appApi.updateBusinessPosition(CurrentUser.getInstance().getmCustomer()), new handleResponse() {
                    @Override
                    public void onSuccess() {
                        finish();
                    }

                    @Override
                    public void onFail(String error) {
                        hide();
                    }
                });
            }
        }else{
            Intent returnIntent = new Intent();
            returnIntent.putExtra("longitude", String.valueOf(marker.getPosition().longitude));
            returnIntent.putExtra("latitude", String.valueOf(marker.getPosition().latitude));
            returnIntent.putExtra("address", t_address.getText().toString());
            setResult(RESULT_OK, returnIntent);
            finish();
        }



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

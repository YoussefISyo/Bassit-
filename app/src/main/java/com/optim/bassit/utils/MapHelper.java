package com.optim.bassit.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.Task;
import com.optim.bassit.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapHelper {

    public static final int ENABLE_LOCATION_REQUEST_CODE = 2018;
    public static final int LOCATION_REQUEST_CODE = 8102;
    private static MapHelper INSTANCE;
    private final LocationRequest locationRequest;


    private MapHelper() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
    }

    public synchronized static MapHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MapHelper();
        }
        return INSTANCE;
    }

    public boolean isLocationPermissionsAccepted(Context context) {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    public boolean isLocationEnabled(Context context) {
        android.location.LocationManager locationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null
                && (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER));
    }

    public void askLocationPermissions(Activity activity) {

        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
    }

    public void askLocationEnable(Activity activity) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices
                .getSettingsClient(activity)
                .checkLocationSettings(builder.build());
        result.addOnCompleteListener(task -> {
            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
            } catch (ApiException exception) {
                switch (exception.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            resolvable.startResolutionForResult(activity, ENABLE_LOCATION_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                        } catch (ClassCastException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    public LocationRequest getLocationRequest() {
        return locationRequest;
    }

    public Marker createUpdateMarker(Context context, GoogleMap map, Marker marker, LatLng latLng, int drawable) {
        if (marker == null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(context, drawable);
            Bitmap bitmap = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), 50, 50, false);
            marker = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
        } else {
            marker.setPosition(latLng);
        }
        return marker;
    }

    public Marker createUpdateMarker(Context context, GoogleMap map, Marker marker, LatLng latLng, int drawable, String title) {
        if (map == null)
            return null;
        if (marker == null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(context, drawable);
            Bitmap bitmap = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), 50, 50, false);
            marker = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
        } else {
            marker.setPosition(latLng);
        }
        marker.setTitle(title);
        return marker;
    }

    public Marker createMarker(Context context, GoogleMap map, Marker marker, LatLng latLng, int drawable, String title) {

        try {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(context, drawable);
            Bitmap bitmap = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), 50, 50, false);
            marker = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

            marker.setTitle(title);
        } catch (Exception ex) {

        }
        return marker;
    }

    public Marker createUpdateMarker(Context context, GoogleMap map, Marker marker, LatLng latLng, int drawable, String title, String snippet) {
        if (marker == null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(context, drawable);
            Bitmap bitmap = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), 50, 50, false);
            marker = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
        } else {
            marker.setPosition(latLng);
        }
        marker.setTitle(title);
        marker.setSnippet(snippet);
        return marker;
    }

    public Marker createUpdateMarker(GoogleMap map, Marker marker, LatLng latLng) {
        if (marker == null) {
            marker = map.addMarker(new MarkerOptions().position(latLng));
        } else {
            marker.setPosition(latLng);
        }
        return marker;
    }


    public Marker createUpdateMarker(GoogleMap map, Marker marker, LatLng latLng, String title, String snippet) {
        if (marker == null) {
            marker = map.addMarker(new MarkerOptions().position(latLng));
        } else {
            marker.setPosition(latLng);
        }
        marker.setTitle(title);
        marker.setSnippet(snippet);
        return marker;
    }

    public void moveCameraToPosition(GoogleMap map, LatLng latLng) {
        if (latLng == null)
            return;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }

    public void moveCameraToPosition(GoogleMap map, LatLng latLng, int zoom) {
        if (latLng == null)
            return;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    public void moveCameraToPosition(GoogleMap map, List<Polyline> polylineList) {
        if (polylineList == null)
            return;
        boolean hasPoints = false;
        Double maxLat = null, minLat = null, minLon = null, maxLon = null;

        for (Polyline polyline : polylineList) {
            if (polyline != null && polyline.getPoints() != null) {
                List<LatLng> pts = polyline.getPoints();
                for (LatLng coordinate : pts) {
                    // Find out the maximum and minimum latitudes & longitudes
                    // Latitude
                    maxLat = maxLat != null ? Math.max(coordinate.latitude, maxLat) : coordinate.latitude;
                    minLat = minLat != null ? Math.min(coordinate.latitude, minLat) : coordinate.latitude;

                    // Longitude
                    maxLon = maxLon != null ? Math.max(coordinate.longitude, maxLon) : coordinate.longitude;
                    minLon = minLon != null ? Math.min(coordinate.longitude, minLon) : coordinate.longitude;

                    hasPoints = true;
                }
            }
        }


        if (hasPoints) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(maxLat, maxLon));
            builder.include(new LatLng(minLat, minLon));
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 48));
        }
    }


    public void zoomCamera(GoogleMap map, int zoom) {
        map.animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }

    public String getAddressStringFromLocation(Context context, LatLng latLng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        if (latLng == null)
            return null;
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder result = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    result.append((i == 0) ? "" : " - ").append(address.getAddressLine(i));
                }
                return !result.toString().isEmpty() ? result.toString() : null;
            } else
                return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Address getAddressFromLocation(Context context, LatLng latLng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        if (latLng == null)
            return null;
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address;
            } else
                return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }

    public void clearLines(List<Polyline> polyLines) {
        if (polyLines != null && !polyLines.isEmpty()) {
            for (Polyline polyline : polyLines) {
                polyline.remove();
            }
            polyLines.clear();
        }
    }

    public double calculateDistance(Location start, Location end) {
        return (start.distanceTo(end) / 1000);
    }

    public double calculateDistance(LatLng latLngStart, LatLng latLngEnd) {
        Location start = new Location("Start");
        start.setLatitude(latLngStart.latitude);
        start.setLongitude(latLngStart.longitude);
        Location end = new Location("End");
        end.setLatitude(latLngEnd.latitude);
        end.setLongitude(latLngEnd.longitude);
        return (start.distanceTo(end) / 1000);
    }


    public double calculateDistance(double latStart, double lngStart, double latEnd, double lngEnd) {
        Location start = new Location("Start");
        start.setLatitude(latStart);
        start.setLongitude(lngStart);
        Location end = new Location("End");
        end.setLatitude(latEnd);
        end.setLongitude(lngEnd);
        return (start.distanceTo(end) / 1000);
    }

    public String distanceString(String latStart, String lngStart, String latEnd, String lngEnd, Context ctx) {
        return distanceString(Double.valueOf(latStart), Double.valueOf(lngStart), Double.valueOf(latEnd), Double.valueOf(lngEnd), ctx);
    }

    public String distanceString(Double latStart, Double lngStart, Double latEnd, Double lngEnd, Context ctx) {
        if (latStart == null || lngStart == null || latEnd == null || lngEnd == null)
            return "";
        if (latStart == 0 && lngStart == 0)
            return "";
        if (latEnd == 0 && lngEnd == 0)
            return "";
        double dist = calculateDistance(latStart, lngStart, latEnd, lngEnd);

        NumberFormat nf = DecimalFormat.getInstance();
        if (dist < 1) {
            nf.setMaximumFractionDigits(0);
            String str = nf.format(Math.floor(dist * 1000));
            return str + " " + ctx.getString(R.string.text_m);
        } else {
            nf.setMaximumFractionDigits(1);
            String str = nf.format(dist);
            return str + " " + ctx.getString(R.string.text_km);
        }
    }


    public LatLngBounds directionBounds(LatLng start, LatLng end) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        builder.include(start);
        builder.include(end);

        LatLngBounds bounds = builder.build();
        return bounds;
    }

    @SuppressLint("MissingPermission")
    public void getGPS(Activity act, finishedGetGPS handler) {

        checkAndRequestLocation(act, (b) -> {
            if (!b)
                handler.onResult(null);
            else {

                LocationManager lm = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);
                List<String> providers = lm.getProviders(true);

                /* Loop over the array backwards, and if you get an accurate location, then break                 out the loop*/
                Location l = null;


                for (int i = providers.size() - 1; i >= 0; i--) {
                    l = lm.getLastKnownLocation(providers.get(i));
                    if (l != null) break;
                }

                double[] gps = new double[2];
                if (l != null) {
                    gps[0] = l.getLatitude();
                    gps[1] = l.getLongitude();
                }
                if (gps[0] == 0 && gps[1] == 0) {
                    handler.onResult(null);
                    return;
                }
                handler.onResult(new LatLng(gps[0], gps[1]));
            }
        });


    }

    private void checkAndRequestLocation(Activity act, finishedRequestingLocation handler) {
        if (!isLocationEnabled(act)) {
            askLocationEnable(act);
            OptimTools.AlertWait(act, R.string.veuillez_activer_votre_position, () -> {
                if (!isLocationEnabled(act)) {
                    handler.onResult(false);
                    return;
                }
            });
        }
        if (!isLocationPermissionsAccepted(act)) {
            askLocationPermissions(act);
            OptimTools.AlertWait(act, R.string.veuillez_autoriser_bassit_a_gps, () -> {
                if (!isLocationPermissionsAccepted(act)) {
                    handler.onResult(false);
                }
            });
            return;
        }
        handler.onResult(true);
    }

    public interface finishedRequestingLocation {
        void onResult(boolean bool);
    }

    public interface finishedGetGPS {
        void onResult(LatLng latLng);
    }
}
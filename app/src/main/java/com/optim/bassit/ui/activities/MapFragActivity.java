package com.optim.bassit.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.base.BaseFragment;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ClusterRenderer;
import com.optim.bassit.models.CustomClusterRenderer;
import com.optim.bassit.models.Customer;
import com.optim.bassit.models.MarkerItem;
import com.optim.bassit.models.MyItem;
import com.optim.bassit.ui.adapters.FlexboxLayoutManagerBugless;
import com.optim.bassit.ui.adapters.InfoWindowAdapter;
import com.optim.bassit.ui.adapters.TagAdapter;
import com.optim.bassit.utils.MapHelper;
import com.optim.bassit.utils.OptimTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    public MapFragActivity() {
    }


    @Inject
    AppApi appApi;
    List<Marker> all;


    public static MapFragActivity newInstance() {
        return new MapFragActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);
        App.getApp(getApplication()).getComponent().inject(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);
    }


   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vwM = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        return vwM;}*/



    Handler handler;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.isMyLocationEnabled();
     //   mMap.clear();
        LatLng ll = new LatLng(Double.valueOf(CurrentUser.getInstance().getmCustomer().getMylat()), Double.valueOf(CurrentUser.getInstance().getmCustomer().getMylon()));
         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 14));


        handler = new Handler();
     //   setUpClusterer();
        loadPins(ll, 20, 10, 100);

        googleMap.setOnCameraChangeListener(cameraPosition -> {

           // clusterManager.cluster();
           // thread(cameraPosition.zoom);

            LatLngBounds bnds = mMap.getProjection().getVisibleRegion().latLngBounds;
            double dist = MapHelper.getInstance().calculateDistance(bnds.northeast, bnds.southwest);


            loadPins(cameraPosition.target, dist, cameraPosition.zoom, 2000);


        });
     /*  mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
              //  mDragTimer.start();
             //  mTimerIsRunning = true;
               //
               // Toast.makeText(MapFragActivity.this,mMap.getCameraPosition().zoom+"",Toast.LENGTH_LONG).show();

            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                // Cleaning all the markers.

               // thread();
                LatLngBounds bnds = mMap.getProjection().getVisibleRegion().latLngBounds;
                double dist = MapHelper.getInstance().calculateDistance(bnds.northeast, bnds.southwest);
                loadPins( mMap.getCameraPosition().target, dist,  mMap.getCameraPosition().zoom, 2000);

           }
        });*/


        mMap.setOnInfoWindowClickListener(this);
    }
    Handler mHandler=new Handler();
    public  void thread(double zoom) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            handleZoom(zoom);
                            //runb=false;
                        }
                    });

                } catch(Exception e){
                }
            }

        }).start();
    }
   private void handleZoom(double zoom) {
        if (all != null)
           // for (Marker m : all)  {
            for (int i=0;i<all.size();i++)  {
                Customer cus = (Customer) all.get(i).getTag();
               /* if (cus == null)

                else*/
                //    all.get(i).setVisible(zoom > cus.getMaplevel());
             //
                all.get(i).setVisible(zoom > 10);

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
                    if(all!=null && all.size()>70)
                    {all.clear(); mMap.clear();}
                    int i=0;
                    for (Customer customer : response.body()) {
                        if(i<50)
                        loadImageAndPutMarker(customer);
                        i++;
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

        if (all != null) {

            for (Marker mm : all) {
                if (mm == null)
                    continue;
                if (((Customer) mm.getTag()).getId() == customer.getId())
                    return;
            }
        }
        int wh = (int) OptimTools.dpToPixels(35, MapFragActivity.this);
        Thread thread = new Thread(() -> {


            Bitmap bmp2 = null;
            try {
                bmp2 = OptimTools.getPicasso(customer.getPinLink() + "/" + customer.getPlan()).get();

            } catch (IOException e) {
                return;
            }

            Bitmap bmp = Bitmap.createScaledBitmap(bmp2, wh, wh, false);

            runOnUiThread(() -> {
                /*InfoWindowAdapter customInfoWindow = new InfoWindowAdapter(MapFragActivity.this);
                mMap.setInfoWindowAdapter(customInfoWindow);*/
                MarkerOptions mo = new MarkerOptions().position(new LatLng(Double.valueOf(customer.getLat()), Double.valueOf(customer.getLon()))).title(customer.getFullName());
                if (bmp != null)
                    mo = mo.icon(BitmapDescriptorFactory.fromBitmap(bmp));


            //    MyItem offsetItem = new MyItem(Double.valueOf(customer.getLat()), Double.valueOf(customer.getLon()), customer.getFullName(), "");
             /*   if(customer.getPlan()==104 || customer.getPlan()==103|| customer.getPlan()==102|| customer.getPlan()==101){

                    Marker marker = mMap.addMarker(mo);
                    marker.setTag(customer);

                }
                    else {*/
                InfoWindowAdapter customInfoWindow = new InfoWindowAdapter(MapFragActivity.this);
                mMap.setInfoWindowAdapter(customInfoWindow);
             //  Marker marker = mMap.addMarker(mo);
               //
                /*MarkerItem markerItem = new MarkerItem(mo,customer,null);
                clusterManager.addItem(markerItem);
                 clusterManager.cluster();*/
            //    }
               // clusterManager.addItem(offsetItem);
               // Marker marker = ;
                if(customer.getPlan()==104)  customer.setMaplevel(0);
                else if(customer.getPlan()==103)  customer.setMaplevel(3);
                else  if(customer.getPlan()==102)  customer.setMaplevel(6);
                else   if(customer.getPlan()==101)  customer.setMaplevel(10);
                else customer.setMaplevel(20);

              //  marker.setTag(customer);
                if (all == null) {
                    all = new ArrayList<>();
                }
                all.add(mMap.addMarker(mo));all.get(all.size()-1).setTag(customer);

            });
        });
        thread.start();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Customer customer = (Customer) marker.getTag();
        if(customer!=null){
        Intent myintent = new Intent(MapFragActivity.this, ProfileActivity.class);
        myintent.putExtra("user_id", customer.getId());
        startActivity(myintent);}
    }
    // Declare a variable for the cluster manager.
 private ClusterManager<MarkerItem> clusterManager;

  /*  private  clusterIconGenerator  val = IconGenerator(context);

    @Override
    public BitmapDescriptor getDescriptorForCluster(Cluster<PromotionMarker> cluster)

    {
        clusterIconGenerator.setBackground(ContextCompat.getDrawable(context, R.drawable.background_marker));
        Bitmap icon  = clusterIconGenerator.makeIcon(cluster.size.toString());
        return BitmapDescriptorFactory.fromBitmap(icon);
    }*/
    MarkerItem markerItemselected;
    Boolean selected=false;
    private void setUpClusterer() {
        // Position the map.
         LatLng ll = new LatLng(Double.valueOf(CurrentUser.getInstance().getmCustomer().getMylat()), Double.valueOf(CurrentUser.getInstance().getmCustomer().getMylon()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 10));
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = new ClusterManager<MarkerItem>(MapFragActivity.this, mMap);
       // clusterManager = new ClusterRenderer<>(this, mMap, clusterManager);
        CustomClusterRenderer renderer = new CustomClusterRenderer(this, mMap, clusterManager);


        clusterManager.setRenderer(renderer);
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
        clusterManager.setOnClusterClickListener(
                new ClusterManager.OnClusterClickListener<MarkerItem>() {
                    @Override public boolean onClusterClick(Cluster<MarkerItem> cluster) {
                        //markerItemselected=null;
                        selected=false;
                      //  Toast.makeText(MapFragActivity.this, "Cluster click", Toast.LENGTH_SHORT).show();

                        // if true, do not move camera

                        return false;
                    }
                });

        clusterManager.setOnClusterItemClickListener( new ClusterManager.OnClusterItemClickListener<MarkerItem>() {
                    @Override
                    public boolean onClusterItemClick(MarkerItem clusterItem) {
                        selected=true;
                        markerItemselected=clusterItem;
                     //   Toast.makeText(MapFragActivity.this, "Cluster item click", Toast.LENGTH_SHORT).show();
                    //  InfoWindowAdapter customInfoWindow = new InfoWindowAdapter(MapFragActivity.this);
                    //    mMap.setInfoWindowAdapter(customInfoWindow);


                        // if true, click handling stops here and do not show info view, do not move camera
                        // you can avoid this by calling:
                        // renderer.getMarker(clusterItem).showInfoWindow();

                        return false;
                    }
                });
      /*  clusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(
                new InfoWindowAdapter(MapFragActivity.this));
        clusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                new InfoWindowAdapter(MapFragActivity.this));*/
        mMap.setOnInfoWindowClickListener(clusterManager);

        clusterManager.setOnClusterInfoWindowClickListener(new ClusterManager.OnClusterInfoWindowClickListener<MarkerItem>() {
            @Override
            public void onClusterInfoWindowClick(Cluster<MarkerItem> cluster) {

            }
        });
        clusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<MarkerItem>() {
            @Override
            public void onClusterItemInfoWindowClick(MarkerItem item) {
                if(selected && item!=null){
                Customer customer = item.getCustomer();
                Intent myintent = new Intent(MapFragActivity.this, ProfileActivity.class);
                myintent.putExtra("user_id", customer.getId());
                startActivity(myintent);}
            }
        });
     //   mMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
       // mMap.setOnCameraChangeListener(clusterManager);
     //   mMap.setOnMarkerClickListener(clusterManager);
        // Add cluster items (markers) to the cluster manager.
     /*  Thread thread = new Thread(() -> {
            runOnUiThread(() -> {
        addItems();
            });
        });
        thread.start();*/
 }

 /*  private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(lat, lng, "Title " + i, "Snippet " + i);
            clusterManager.addItem(offsetItem);
        }
    }*/
/* public  class MyClusterRenderer extends DefaultClusterRenderer<MarkerItem> {
     private final IconGenerator mClusterIconGenerator = new IconGenerator(
            MapFragActivity.this);
     public MyClusterRenderer(Context context, GoogleMap map,
                              ClusterManager<MarkerItem> clusterManager) {
         super(context, map, clusterManager);


         View multiProfile = getLayoutInflater().inflate(
                 R.layout.cluster_custome_icon, null);
         mClusterIconGenerator.setContentView(multiProfile);
     }

     @Override
     protected void onBeforeClusterItemRendered(MarkerItem item, MarkerOptions markerOptions) {

         markerOptions.title("").icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_both));

         super.onBeforeClusterItemRendered(item, markerOptions);
     }

     //        @Override
//        protected void onClusterItemRendered(final MyItem clusterItem, Marker marker) {
//            super.onClusterItemRendered(clusterItem, marker);
//
//        }
     @Override
     protected void onBeforeClusterRendered(Cluster<MarkerItem> cluster,
                                            MarkerOptions markerOptions) {

         Log.e("get_item_list_nir", "CallMap onBeforeClusterRendered 13");
         try {
             mClusterIconGenerator.setBackground(null);
             Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster
                     .getSize()));
             markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
         } catch (Exception e) {
             e.printStackTrace();
             Log.e("get_item_list_nir", "error 13.1 : " + e.toString());
         }
         Log.e("get_item_list_nir", "CallMap onBeforeClusterRendered 14");
     }
 }*/
 /*public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
     private Context context;

     public InfoWindowAdapter(Context ctx) {
         context = ctx;
     }

     @Override
     public View getInfoWindow(Marker marker) {
         return null;
     }

     @Override
     public View getInfoContents(Marker marker) {
         if(selected==false)
             return null;
         View view = ((Activity) context).getLayoutInflater()
                 .inflate(R.layout.map_custom_infowindow, null);


         TextView t_apropos = view.findViewById(R.id.t_apropos);
         TextView t_description = view.findViewById(R.id.t_description);
         RecyclerView recyclerView = view.findViewById(R.id.rvTags);
         FlexboxLayoutManagerBugless layoutManager = new FlexboxLayoutManagerBugless(view.getContext());

         recyclerView.setLayoutManager(layoutManager);
         TagAdapter tagAdapter = new TagAdapter(false, null);
         recyclerView.setAdapter(tagAdapter);

         Customer customer = markerItemselected.getCustomer();
         if (customer.getCat() != null) {
             List<String> tags = new ArrayList<>(Arrays.asList(customer.getCat().split(";")));
             tagAdapter.fill(tags);
         }
         t_apropos.setText(customer.getFullName());
         t_description.setText(customer.getDescription());


         return view;
     }

 }*/
}
package com.optim.bassit.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.optim.bassit.R;
/*
public  class MyClusterRenderer extends DefaultClusterRenderer<MyItem> {
        private final IconGenerator mClusterIconGenerator = new IconGenerator(  );
        public MyClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<MyItem> clusterManager, IconGenerator mClusterIconGenerator) {
            super(context, map, clusterManager);



            View multiProfile ;//= context.geta.getLayoutInflater().inflate( R.layout.cluster_custome_icon, null);
            this.mClusterIconGenerator.setContentView(multiProfile);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {

         markerOptions.title("").icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_both));

            super.onBeforeClusterItemRendered(item, markerOptions);
        }

//        @Override
//        protected void onClusterItemRendered(final MyItem clusterItem, Marker marker) {
//            super.onClusterItemRendered(clusterItem, marker);
//
//        }
        @Override
        protected void onBeforeClusterRendered(Cluster<MyItem> cluster,
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
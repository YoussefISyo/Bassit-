package com.optim.bassit.models;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.optim.bassit.R;

/**
 * Created by: Anton Shkurenko (tonyshkurenko)
 * Project: ClusterManagerDemo
 * Date: 6/7/16
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class CustomClusterRenderer extends DefaultClusterRenderer<MarkerItem> {

  private final IconGenerator mClusterIconGenerator;
  private final Context mContext;

  public CustomClusterRenderer(Context context, GoogleMap map,
                               ClusterManager<MarkerItem> clusterManager) {
    super(context, map, clusterManager);

    mContext = context;
    mClusterIconGenerator = new IconGenerator(mContext.getApplicationContext());
  }

 @Override
  protected void onBeforeClusterItemRendered(MarkerItem item,
                                             MarkerOptions markerOptions) {

  /*  final BitmapDescriptor markerDescriptor =
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
*/
    markerOptions.icon(item.getIcon()).snippet(item.getTitle());
  }
 /* @NonNull
  @Override
  protected void onBeforeClusterRendered( @NonNull Cluster<MarkerItem> cluster,
                                         MarkerOptions markerOptions) {

    mClusterIconGenerator.setBackground( ContextCompat.getDrawable(mContext, R.drawable.ic_cluster_location));

    mClusterIconGenerator.setTextAppearance(R.style.AppTheme_WhiteTextAppearance);

    Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
  }*/
  //private  IconGenerator clusterIconGenerator  ;
 @NonNull
  @Override
  protected BitmapDescriptor getDescriptorForCluster(@NonNull Cluster<MarkerItem> cluster) {
    mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_cluster_location));
    mClusterIconGenerator.setTextAppearance(R.style.AppTheme_WhiteTextAppearance);
    Bitmap icon  = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
    return BitmapDescriptorFactory.fromBitmap(icon);
   // return super.getDescriptorForCluster(cluster);
  }
}
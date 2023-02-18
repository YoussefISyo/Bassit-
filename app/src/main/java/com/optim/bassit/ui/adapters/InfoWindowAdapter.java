package com.optim.bassit.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.optim.bassit.R;
import com.optim.bassit.models.Customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
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
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.map_custom_infowindow, null);


        TextView t_apropos = view.findViewById(R.id.t_apropos);
        TextView t_description = view.findViewById(R.id.t_description);
        RecyclerView recyclerView = view.findViewById(R.id.rvTags);
        FlexboxLayoutManagerBugless layoutManager = new FlexboxLayoutManagerBugless(view.getContext());

        recyclerView.setLayoutManager(layoutManager);
        TagAdapter tagAdapter = new TagAdapter(false, null);
        recyclerView.setAdapter(tagAdapter);

        Customer customer = (Customer) marker.getTag();
        if (customer.getCat() != null) {
            List<String> tags = new ArrayList<>(Arrays.asList(customer.getCat().split(";")));
            tagAdapter.fill(tags);
        }
        t_apropos.setText(customer.getFullName());
        t_description.setText(customer.getDescription());


        return view;
    }

}


package com.example.running_app.data.model;

import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class PolylineUpdater {
    private Polyline polyline;
    private final List<LatLng> polylinePoints = new ArrayList<>();
    private final GoogleMap mGoogleMap;

    public PolylineUpdater(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    public void updatePolyline(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        polylinePoints.add(latLng);

        Log.d("GPS@@", "polylinePoints" + polylinePoints);

        if (polyline != null) {
            polyline.remove();
        }

        PolylineOptions polylineOptions = new PolylineOptions().addAll(polylinePoints).color(Color.BLUE).width(10f).geodesic(true);

        polyline = mGoogleMap.addPolyline(polylineOptions);
    }

}

package com.example.running_app.data.model;

import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.example.running_app.ui.fragments.RunFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class PolylineMarkerUpdater {
    private Polyline polyline;
    private List<LatLng> polylinePoints = new ArrayList<>();
    private GoogleMap mGoogleMap;
    private Marker mCurrentLocationMarker;

    public PolylineMarkerUpdater(GoogleMap googleMap) {
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

    public void updateMarker(Location location) {
        if (polylinePoints.size() > 1) {
            mCurrentLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mCurrentLocationMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("마포")
                .snippet("현재위치"));

    }
}

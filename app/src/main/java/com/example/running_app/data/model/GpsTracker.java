package com.example.running_app.data.model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class GpsTracker extends Service implements LocationListener {

    private final Context mContext;
    protected LocationManager locationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1;
    Location location;
    double latitude;
    double longitude;
    private updateMap mListener;


    public GpsTracker(Context mContext, updateMap listener) {
        this.mContext = mContext;
        mListener = listener;
        getLocation();
    }

    @SuppressLint("ServiceCast")
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Log.d("GPS@@", "isGPSEnabled : " + isGPSEnabled + " isNetworkEnabled : " + isNetworkEnabled);


            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);

                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

                } else return null;

                if (isNetworkEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                        } else {
                            Toast.makeText(mContext, "location 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            if (isGPSEnabled) {
                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (location == null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                }
            }
        } catch (Exception e) {
            Log.d("GPS@@", "" + e.toString());
        }

        return location;
    }


    public void updateLocation(Location location) {
        if (location != null) {
            this.location = location;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("GPS@@", "latitude" + latitude);
            Log.d("GPS@@", "longitude" + longitude);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        updateLocation(location);

        Toast.makeText(mContext, "현재위치 LC \n위도 " + location.getLatitude() + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
        Toast.makeText(mContext, location.getProvider(), Toast.LENGTH_LONG).show();

        if(mListener != null) mListener.updateMap(location);
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();

        }

        return latitude;
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GpsTracker.this);
        }
    }

    public interface updateMap{
        void updateMap(Location location);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}

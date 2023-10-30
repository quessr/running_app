package com.example.running_app.data.model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.running_app.R;

public class GpsTrackerService extends Service implements LocationListener {

    private final Context mContext;
    protected LocationManager locationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1;
    Location location;
    double latitude;
    double longitude;
    private updateMap mListener;

    public GpsTrackerService() {
        mContext = this;
        mListener = null;
        Log.d("HSR", "GpsTracker no arg ");
    }

    public GpsTrackerService(Context mContext, updateMap listener) {
        this.mContext = mContext;
        mListener = listener;
        getLocation();
        Log.d("HSR", "GpsTracker two arg ");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("HSR", "onCreate");

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

        if (mListener != null) mListener.updateMap(location);
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
            locationManager.removeUpdates(GpsTrackerService.this);
        }
    }

    public interface updateMap {
        void updateMap(Location location);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("HSR", "onStartCommand");

        startForeground(1, createNotification());
        return START_STICKY;
    }

    @SuppressLint("MissingPermission")
    private Notification createNotification() {

        Log.d("HSR", "createNotification");

        String channelId = "gps_tacker_channel";
        int notificationId = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "GPS Tracker Channel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("GPS Traker")
                .setContentText("Tracking your location...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(notificationId, builder.build());
        return  builder.build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}

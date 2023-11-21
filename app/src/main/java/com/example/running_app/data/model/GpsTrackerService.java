package com.example.running_app.data.model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.running_app.R;


public class GpsTrackerService extends Service implements LocationListener {
    //    private final Context mContext;
    protected LocationManager locationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1;
    Location location;
    double latitude;
    double longitude;
    private updateMap mListener;
    private NotificationManager notificationManager;
    int notificationId = 0;

    private final IBinder mBinder = new LocalBinder();


    public class LocalBinder extends Binder {
        public GpsTrackerService getService() {
            return GpsTrackerService.this;
        }
    }

    public void startLocationUpdate() {
        getLocation();
    }

    @SuppressLint("ServiceCast")
    public void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Log.d("GPS@@", "isGPSEnabled : " + isGPSEnabled + " isNetworkEnabled : " + isNetworkEnabled);


            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(getApplicationContext(), "Network, Gps 연결이 없습니다.", Toast.LENGTH_SHORT).show();

            } else {
                int hasFineLocationPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
                    if (isNetworkEnabled) {
                        if (locationManager != null) {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                            if (location == null) {
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            }

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                            } else {
                                Toast.makeText(this, "location 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                                // 네트워크 기반 위치 정보 비활성화 상태에서의 예외 처리

                                // 와이파이 설정 창을 띄우기 위한 인텐트 생성
                                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                startActivity(intent);
                            }
                        }
                    } else {
//                        Toast.makeText(getApplicationContext(), "Network 연결이 없습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("GPS@@", "Network 연결이 없습니다.");


                    }
                    if (isGPSEnabled) {

                        if (locationManager != null) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

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

            }


        } catch (Exception e) {
            Log.d("GPS@@", "" + e);
        }
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

        Toast.makeText(getApplicationContext(), "현재위치 " + location.getProvider() + " \n위도 " + location.getLatitude() + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
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

    public void setListener(updateMap listener) {
        mListener = listener;
    }

    public interface updateMap {
        void updateMap(Location location);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("HSR", "onStartCommand");
        return START_STICKY;
    }

    public void startForeground() {
        startForeground(1, createNotification());
    }

    @SuppressLint("MissingPermission")
    private Notification createNotification() {

        Log.d("HSR", "createNotification");

        String channelId = "gps_tacker_channel";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "GPS Tracker Channel", NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("RUNNING APP")
                .setContentText("Tracking your location...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return builder.build();
    }

    @Override
    public boolean stopService(Intent name) {
        Toast.makeText(getApplicationContext(), "서비스가 중지 되었습니다.", Toast.LENGTH_SHORT).show();

        return super.stopService(name);
    }

    public void stopNotification() {
        if (notificationManager != null) {
            notificationManager.cancel(notificationId);
            stopForeground(true);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}

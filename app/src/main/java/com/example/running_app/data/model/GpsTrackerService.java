package com.example.running_app.data.model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
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
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.running_app.R;
import com.example.running_app.data.database.dao.GpsDao;
import com.example.running_app.data.database.dao.RunDao;
import com.example.running_app.data.database.dao.RunDatabase;
import com.example.running_app.data.database.dao.TB_GPS;
import com.example.running_app.data.database.dao.TB_Run;
import com.example.running_app.ui.MainActivity;
import com.example.running_app.ui.viewmodels.RunViewModel;
import com.example.running_app.ui.viewmodels.TimerViewModel;


public class GpsTrackerService extends Service implements LocationListener {

    public static final int ERROR_CODE_GPS_DISABLE = 0;
    public static final int ERROR_CODE_NETWORK_DISABLE = 1;
    public static final int ERROR_CODE_GPS_ENABLE = 2;
    public static final int ERROR_CODE_NETWORK_ENABLE = 3;

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


            if (!isGPSEnabled && !isNetworkEnabled) {   //gps랑 네트워크 둘다 안될때 예외처리

                mListener.occurError(ERROR_CODE_GPS_DISABLE);
                mListener.occurError(ERROR_CODE_NETWORK_DISABLE);

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
                                Toast.makeText(MainActivity.mContext, "location 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                                // 네트워크 기반 위치 정보 비활성화 상태에서의 예외 처리
                            }
                        }
                    } else {
                        mListener.occurError(ERROR_CODE_NETWORK_DISABLE);
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location == null) {
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            }

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }

                    }
                } else {
                    mListener.occurError(ERROR_CODE_GPS_DISABLE);
                }
            }


        } catch (Exception e) {
            Log.d("GPS@@", "" + e.toString());
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
        // updateLocation(location);


        Toast.makeText(getApplicationContext(), "현재위치 " + location.getProvider() + " \n위도 " + location.getLatitude() + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
        if (mListener != null) mListener.updateMap(location);
    }

//    public double getLongitude() {
//        if (location != null) {
//            longitude = location.getLongitude();
//        }
//
//        return longitude;
//    }

//    public double getLatitude() {
//        if (location != null) {
//            latitude = location.getLatitude();
//
//        }
//
//        return latitude;
//    }

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

        //오류 발생 될 경우
        void occurError(int errorCode);

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
                .setContentTitle("GPS Traker")
                .setContentText("Tracking your location...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(notificationId, builder.build());
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

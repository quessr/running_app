package com.example.running_app.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.running_app.R;
import com.example.running_app.data.model.GpsTrackerService;
import com.example.running_app.data.model.PolylineMarkerUpdater;
import com.example.running_app.databinding.FragmentRunBinding;
import com.example.running_app.ui.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class RunFragment extends Fragment implements OnMapReadyCallback, GpsTrackerService.updateMap, LocationListener {
    private FragmentRunBinding binding;
    private GpsTrackerService gpsTracker;
    public GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;

    private int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private PolylineMarkerUpdater polylineMarkerUpdater;
    private Marker initialMapMarker;
    private Marker runStartMapMarker;

    protected LocationManager locationManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) MainActivity.mContext.getSystemService(Context.LOCATION_SERVICE);

        // 권한 체크
        if (ContextCompat.checkSelfPermission(MainActivity.mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);



        } else {
            // 권한이 없다면 권한을 요청해야 합니다.
            ActivityCompat.requestPermissions((Activity) MainActivity.mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRunBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // 구글 맵 띄우기
        mapFragment = new SupportMapFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);

        } else {
            Log.e("RunFragment", "mapFragment is null");
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
        // 권한 체크
        if (ContextCompat.checkSelfPermission(MainActivity.mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED
                || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_DENIED
                || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            // 권한이 없는 경우 사용자에게 권한을 요청하는 다이얼로그 띄우기
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.FOREGROUND_SERVICE, Manifest.permission.ACTIVITY_RECOGNITION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {

            ActivityCompat.requestPermissions((Activity) MainActivity.mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACTIVITY_RECOGNITION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }

        return view;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d("HSR", "onMapReady()");
        double lastLatitude = 0;
        double lastLongitude = 0;
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setBuildingsEnabled(true);

        polylineMarkerUpdater = new PolylineMarkerUpdater(mGoogleMap);

        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void updateMap(Location location) {
        Log.d("HSR", "RunFragment : " + location);
//        if (location.getProvider().equals("gps")) {
//            polylineMarkerUpdater.updatePolyline(location);
//            polylineMarkerUpdater.updateMarker(location);
//        }

        LatLng lastKnownLocation = new LatLng(location.getLatitude(), location.getLongitude());

        if (runStartMapMarker == null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, 15));

            runStartMapMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(lastKnownLocation)
                    .title("마포")
                    .snippet("처음위치")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        } else {
            runStartMapMarker.setPosition(lastKnownLocation);
            polylineMarkerUpdater.updatePolyline(location);
        }

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

        double lastLatitude = location.getLatitude();
        double lastLongitude = location.getLongitude();

        LatLng lastKnownLocation = new LatLng(lastLatitude, lastLongitude);

        if (initialMapMarker == null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, 15));

            initialMapMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(lastKnownLocation)
                    .title("마포")
                    .snippet("처음위치")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            // 지도 회전
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(lastKnownLocation)
                    .bearing(180)                  // 180도 회전
                    .zoom(18)
                    .build();


            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
            initialMapMarker.setPosition(lastKnownLocation);
        }

    }
}

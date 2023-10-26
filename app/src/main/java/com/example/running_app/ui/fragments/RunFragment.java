package com.example.running_app.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.running_app.R;
import com.example.running_app.data.model.GpsTracker;
import com.example.running_app.databinding.FragmentRunBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class RunFragment extends Fragment implements OnMapReadyCallback, GpsTracker.updateMap {
    private FragmentRunBinding binding;
    private GpsTracker gpsTracker;
    private GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;

    private int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private Polyline polyline;
    private List<LatLng> polylinePoints = new ArrayList<>();
    private Marker currentLocationMarker;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gpsTracker = new GpsTracker(getContext(), this);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRunBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // 구글 맵 띄우기
        mapFragment = new SupportMapFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e("RunFragment", "mapFragment is null");
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 경우 사용자에게 권한을 요청하는 다이얼로그 띄우기
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            double lastLatitude = gpsTracker.getLatitude();
            double lastLongitude = gpsTracker.getLongitude();

            Log.d("GPS_LOCATION", "lastLatitude :" + lastLatitude + " lastLongitude :" + lastLongitude);
        }

        return view;
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        double lastLatitude = gpsTracker.getLatitude();
        double lastLongitude = gpsTracker.getLongitude();

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setBuildingsEnabled(true);

        LatLng lastKnownLocation = new LatLng(lastLatitude, lastLongitude);


        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, 18));

        googleMap.addMarker(new MarkerOptions()
                .position(lastKnownLocation)
                .title("마포")
                .snippet("처음위치")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

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
            currentLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        currentLocationMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("마포")
                .snippet("현재위치"));

    }

    @Override
    public void updateMap(Location location) {
        if (location.getProvider().equals("gps")) {
            updatePolyline(location);
            updateMarker(location);
        }
    }
}

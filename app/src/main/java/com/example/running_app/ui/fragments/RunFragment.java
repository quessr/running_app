package com.example.running_app.ui.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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

public class RunFragment extends Fragment implements OnMapReadyCallback, GpsTrackerService.updateMap {
    private FragmentRunBinding binding;
    private GpsTrackerService gpsTracker;
    public GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;

    private int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private PolylineMarkerUpdater polylineMarkerUpdater;
    private Marker currentLocationMarker;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
//            binding.runStartBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getContext(), RunStartCountdownFragment.class);
//                    startActivity(intent);
//                }
//            });

        } else {
            Log.e("RunFragment", "mapFragment is null");
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 경우 사용자에게 권한을 요청하는 다이얼로그 띄우기
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.FOREGROUND_SERVICE},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
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

        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
        if(((MainActivity)getActivity()).gpsTracker != null){
            lastLatitude = ((MainActivity)getActivity()).gpsTracker.getLatitude();
            lastLongitude = ((MainActivity)getActivity()).gpsTracker.getLongitude();
            LatLng lastKnownLocation = new LatLng(lastLatitude, lastLongitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, 15));
            googleMap.addMarker(new MarkerOptions()
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

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    double lastLatitude = ((MainActivity)getActivity()).gpsTracker.getLatitude();
                    double lastLongitude = ((MainActivity)getActivity()).gpsTracker.getLongitude();
                    LatLng lastKnownLocation = new LatLng(lastLatitude, lastLongitude);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, 15));
                    googleMap.addMarker(new MarkerOptions()
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

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }, 1000);

        }
    }

    @Override
    public void updateMap(Location location) {
        Log.d("HSR", "RunFragment : "+location);
        if (location.getProvider().equals("gps")) {
            polylineMarkerUpdater.updatePolyline(location);
            polylineMarkerUpdater.updateMarker(location);
        }
    }

}

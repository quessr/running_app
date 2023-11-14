package com.example.running_app.ui.fragments;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.running_app.R;
import com.example.running_app.data.model.GpsTrackerService;
import com.example.running_app.data.model.PermissionManager;
import com.example.running_app.data.model.PolylineMarkerUpdater;
import com.example.running_app.databinding.FragmentRunBinding;
import com.example.running_app.ui.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RunFragment extends Fragment implements OnMapReadyCallback, GpsTrackerService.updateMap, LocationListener {
    private FragmentRunBinding binding;
    public GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;

    private int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private PolylineMarkerUpdater polylineMarkerUpdater;
    private Marker initialMapMarker;
    private Marker runStartMapMarker;

    protected LocationManager locationManager;

    Circle circle;
    private AlertDialog permissionDeniedDialog;
    PermissionManager permissionManager;
    public ActivityResultLauncher<String[]> requestPermissionLauncher;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionManager = new PermissionManager();
        permissionManager.requestPermission(getContext(), this);


        Log.d("HSR", "onCreate()");
        locationManager = (LocationManager) MainActivity.mContext.getSystemService(Context.LOCATION_SERVICE);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                    @Override
                    public void onActivityResult(Map<String, Boolean> result) {
                        for (Map.Entry<String, Boolean> entry : result.entrySet()) {
                            String permission = entry.getKey();
                            boolean isGranted = entry.getValue();

                            if (isGranted) {
                                // 권한이 허용된 경우 처리할 코드
                                Toast.makeText(getContext(), permission + " 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                // 권한이 거부된 경우 처리할 코드
                                Toast.makeText(getContext(), permission + " 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
                                Log.d("HHH", "onActivityResult fail");
                                permissionManager.showPermissionDeniedNotification(getActivity());

                            }
                        }
                    }
                });


    }

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRunBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Log.e("HSR", "onCreateView");


        try {
            Log.e("HSR", "onCreateView try");


            // 구글 맵 띄우기
            mapFragment = new SupportMapFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

            if (mapFragment != null) {
                mapFragment.getMapAsync(this);

            } else {
                Log.e("RunFragment", "mapFragment is null");
            }


            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);


        } catch (Exception e) {
            Log.d("HHH", e.toString());
        }

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        permissionManager.requestPermission(requireContext(), this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onStart() {
        super.onStart();
        Log.d("HSR", "onStart()");

        if (permissionManager.hasLocationPermissions(requireContext())) {
            // 위치 권한이 허용된 경우 처리할 코드
            // ...
            Log.d("HSR", "onStart() hasLocationPermissions");
            Log.d("HSR", "onStart() permissionDeniedDialog" + permissionDeniedDialog);

            Toast.makeText(getContext(), "권한이 모두 허용되었습니다.", Toast.LENGTH_SHORT).show();

            locationManager = (LocationManager) MainActivity.mContext.getSystemService(Context.LOCATION_SERVICE);

            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);

            if (permissionDeniedDialog != null && permissionDeniedDialog.isShowing()) {
                permissionDeniedDialog.dismiss();
                permissionDeniedDialog.cancel();
                Log.d("HSR", "onStart() permissionDeniedDialog.dismiss()");

            }

        } else {
            // 위치 권한이 거부된 경우 처리할 코드
            // 다이얼로그가 표시된 후 권한을 허용한 경우 처리할 코드
            Log.d("HSR", "onStart() permissionDeniedDialog" + permissionDeniedDialog);

            Toast.makeText(getContext(), "백그라운드 위치 권한을 위해 항상 허용으로 설정해주세요.", Toast.LENGTH_SHORT).show();
        }
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
            String currentMarkerTitle = (getAddress(getContext(), location.getLatitude(), location.getLongitude()));

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, 15));

            runStartMapMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(lastKnownLocation)
                    .title(currentMarkerTitle)
                    .snippet(getResources().getString(R.string.map_marker_current_location))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            CircleOptions circleOptions = new CircleOptions()
                    .center(lastKnownLocation)
                    .radius(100)
                    .strokeWidth(0)// 원의 반지름 설정 (미터 단위)
                    .fillColor(Color.parseColor("#33FF0000")); // 내부 색상 설정
            circle = mGoogleMap.addCircle(circleOptions);

            // Circle 깜빡임 애니메이션 적용
            ValueAnimator animator = new ValueAnimator();
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setIntValues(0, 100);
            animator.setDuration(2000); // 애니메이션 속도를 조절합니다.
            animator.addUpdateListener(valueAnimator -> {
                int value = (int) valueAnimator.getAnimatedValue();
                circle.setRadius(value); // 원의 반지름을 변경하여 깜빡이는 효과를 줍니다.
            });
            animator.start();

        } else {
            runStartMapMarker.setPosition(lastKnownLocation);
            circle.setCenter(lastKnownLocation);
            polylineMarkerUpdater.updatePolyline(location);
        }

    }

    @Override
    public void occurError(int errorCode) {


    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

        double lastLatitude = location.getLatitude();
        double lastLongitude = location.getLongitude();

        LatLng lastKnownLocation = new LatLng(lastLatitude, lastLongitude);

        if (initialMapMarker == null) {
            String initialMarkerTitle = (getAddress(getContext(), lastLatitude, lastLongitude));

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, 15));

            initialMapMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(lastKnownLocation)
                    .title(initialMarkerTitle)
                    .snippet(getResources().getString(R.string.map_marker_first_position))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        } else {
        }

    }

    public String getAddress(Context mContext, double lat, double lng) {
        String nowAddr = "현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;

        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);
                if (address != null && address.size() > 0) {
                    nowAddr = address.get(0).getAddressLine(0).toString();
                }
            }
        } catch (IOException e) {
            Toast.makeText(mContext, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return nowAddr;
    }
}

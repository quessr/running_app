package com.example.running_app.ui.fragments;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) MainActivity.mContext.getSystemService(Context.LOCATION_SERVICE);

    }

    @SuppressLint("MissingPermission")
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

        // 권한 체크
//        if (ContextCompat.checkSelfPermission(MainActivity.mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
//                || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED
//                || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_DENIED
//                || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
//            // 권한이 없는 경우 사용자에게 권한을 요청하는 다이얼로그 띄우기
//            ActivityCompat.requestPermissions((Activity) MainActivity.mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACTIVITY_RECOGNITION}, MY_PERMISSIONS_REQUEST_LOCATION);
//        }

        requestPermission();


        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);

        return view;
    }

    private void backgroundPermission() {
        ActivityCompat.requestPermissions(
                requireActivity(),
                new String[]{android.Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                2
        );
    }

    private void permissionDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("백그라운드 위치 권한을 위해 항상 허용으로 설정해주세요.");

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_POSITIVE) {
                    backgroundPermission();
                }
            }
        };

        builder.setPositiveButton("네", listener);
        builder.setNegativeButton("아니오", null);

        builder.show();
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

    private void requestPermission() {
        // 이미 권한이 있으면 그냥 리턴
        if (hasLocationPermissions(requireContext())) {
            return;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                        requireActivity(),
                        new String[]{
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACTIVITY_RECOGNITION
                        },
                        1
                );
                permissionDialog(requireContext());
            } else {
                ActivityCompat.requestPermissions(
                        requireActivity(),
                        new String[]{
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACTIVITY_RECOGNITION
                        },
                        1
                );
            }
        }
    }


        public static boolean hasLocationPermissions(Context context) {
            int fineLocationPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
            int coarseLocationPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
            int backgroundLocationPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            int recognitionPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION);

            return fineLocationPermission == PackageManager.PERMISSION_GRANTED && coarseLocationPermission == PackageManager.PERMISSION_GRANTED && backgroundLocationPermission == PackageManager.PERMISSION_GRANTED && recognitionPermission == PackageManager.PERMISSION_GRANTED;
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

//            runStartMapMarker = mGoogleMap.addMarker(new MarkerOptions()
//                    .position(lastKnownLocation)
//                    .title("마포")
//                    .snippet("처음위치")
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            runStartMapMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(lastKnownLocation)
                    .title("마포")
                    .snippet("처음위치")
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
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            // 지도 회전
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(lastKnownLocation)
                    .bearing(180)                  // 180도 회전
                    .zoom(15)
                    .build();


            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
//            initialMapMarker.setPosition(lastKnownLocation);
        }

    }
}

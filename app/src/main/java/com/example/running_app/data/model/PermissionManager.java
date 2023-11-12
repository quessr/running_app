package com.example.running_app.data.model;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import com.example.running_app.ui.MainActivity;
import com.example.running_app.ui.fragments.RunFragment;

import java.util.Map;

public class PermissionManager {

    RunFragment runFragment = new RunFragment();
    private AlertDialog permissionDeniedDialog;
    public static boolean hasLocationPermissions(Context context) {
        int fineLocationPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        int backgroundLocationPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        int recognitionPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION);

        return fineLocationPermission == PackageManager.PERMISSION_GRANTED && coarseLocationPermission == PackageManager.PERMISSION_GRANTED && backgroundLocationPermission == PackageManager.PERMISSION_GRANTED && recognitionPermission == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(Context context, RunFragment runFragment) {
        // 이미 권한이 있으면 그냥 리턴
        if (hasLocationPermissions(context)) {
            return;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (runFragment.requestPermissionLauncher != null) {
                    runFragment.requestPermissionLauncher.launch(new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACTIVITY_RECOGNITION
                    });
                }
//                permissionDialog(requireContext());
            } else {
                if (runFragment.requestPermissionLauncher != null) {
                    runFragment.requestPermissionLauncher.launch(new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACTIVITY_RECOGNITION
                    });
                }
            }
        }
    }


//    private ActivityResultLauncher<String[]> requestPermissions = registerForActivityResult(
//            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
//                @Override
//                public void onActivityResult(Map<String, Boolean> result) {
//                    for (Map.Entry<String, Boolean> entry : result.entrySet()) {
//                        String permission = entry.getKey();
//                        boolean isGranted = entry.getValue();
//
//                        if (isGranted) {
//                            // 권한이 허용된 경우 처리할 코드
//                            // ...
//                            Toast.makeText(getContext(), permission + " 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            // 권한이 거부된 경우 처리할 코드
//                            // ...
//                            Toast.makeText(getContext(), permission + " 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
//                            Log.d("HHH", "onActivityResult fail");
//                            showPermissionDeniedNotification();
//
//                        }
//                    }
//                }
//            });

//    public void showPermissionDeniedNotification(Activity activity) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle("위치 권한 거부")
//                .setMessage("앱을 사용하기 위해서는 위치 권한이 필요합니다. 위치 권한을 허용해주세요.")
//                .setPositiveButton("설정으로 이동", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 설정 화면으로 이동
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
//                        intent.setData(uri);
//                        activity.startActivity(intent);
//                    }
//                })
//                .setNegativeButton("종료", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        activity.finish();;
//                    }
//                })
//                .setCancelable(false)
//                .create()
//                .show();
//    }

    public void showPermissionDeniedNotification(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("필수 권한 거부")
                .setMessage("앱을 사용하기 위해서는 위치 및 신체 활동 권한이 필요합니다. \n거부된 권한을 허용해주시기 바랍니다.")
                .setPositiveButton("설정으로 이동", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 설정 화면으로 이동
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        requireActivity().finish();
                        permissionDeniedDialog.dismiss();
                    }
                })
                .setCancelable(false);


        permissionDeniedDialog = builder.create();

        permissionDeniedDialog.show();
    }
}



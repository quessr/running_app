package com.example.running_app.data.model;


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

import androidx.core.app.ActivityCompat;

import com.example.running_app.ui.fragments.RunFragment;

public class PermissionManager {

    public AlertDialog permissionDeniedDialog;

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

    public void showPermissionDeniedNotification(Activity activity, String message, String action, String setting_screen_type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("필수 권한 거부")
        builder.setTitle(activity.getResources().getString(R.string.show_permission_denied_notification_title))
                .setMessage(message)
                .setPositiveButton("설정으로 이동", new DialogInterface.OnClickListener() {
                .setPositiveButton(activity.getResources().getString(R.string.show_permission_denied_notification_positive_button), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 설정 화면으로 이동
                        if ("app_settings".equals(setting_screen_type)) {
                            Intent intent = new Intent(action);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            intent.setData(uri);
                            activity.startActivity(intent);
                        } else if ("location_setting".equals(setting_screen_type)) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            activity.startActivity(intent);
                        }

                    }
                })
                .setNegativeButton("종료", new DialogInterface.OnClickListener() {
                .setNegativeButton(activity.getResources().getString(R.string.show_permission_denied_notification_negative_button), new DialogInterface.OnClickListener() {
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



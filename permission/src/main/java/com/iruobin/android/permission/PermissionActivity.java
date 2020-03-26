package com.iruobin.android.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PermissionActivity extends Activity {

    public static final String KEY_PERMISSIONS = "permissions";
    private static final int RC_REQUEST_PERMISSION = 100;
    private static PermissionCallback CALLBACK;

    public static void request(Context context, String[] permissions, PermissionCallback callback) {
        CALLBACK = callback;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_PERMISSIONS, permissions);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (!intent.hasExtra(KEY_PERMISSIONS)) {
            return;
        }
        // 当api大于23时，才进行权限申请
        String[] permissions = getIntent().getStringArrayExtra(KEY_PERMISSIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, RC_REQUEST_PERMISSION);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != RC_REQUEST_PERMISSION) {
            return;
        }
        // 处理申请结果
        boolean[] shouldShowRequestPermissionRationale = new boolean[permissions.length];
        for (int i = 0; i < permissions.length; ++i) {
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i]);
        }
        this.onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale);
    }


    @TargetApi(Build.VERSION_CODES.M)
    void onRequestPermissionsResult(String[] permissions, int[] grantResults, boolean[] shouldShowRequestPermissionRationale) {
        int length = permissions.length;
        int granted = 0;
        List<String> rationalPermissions = new ArrayList<>();
        List<String> rejectedPermissions = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale[i]){
                    Log.d(Permission.TAG, "shouldShowPermissionRationale: " + permissions[i]);
                    CALLBACK.shouldShowPermissionRationale(permissions[i]);
                    rationalPermissions.add(permissions[i]);
                } else {
                    Log.d(Permission.TAG, "onPermissionRejected: " + permissions[i]);
                    CALLBACK.onPermissionRejected(permissions[i]);
                    rejectedPermissions.add(permissions[i]);
                }
            } else {
                Log.d(Permission.TAG, "onPermissionGranted: " + permissions[i]);
                CALLBACK.onPermissionGranted(permissions[i]);
                granted++;
            }
        }

        if (granted == length) {
            Log.d(Permission.TAG, "onPermissionsGranted: " + permissions.length);
            CALLBACK.onPermissionsGranted(permissions);
        } else {
            if (rationalPermissions.size() > 0) {
                Log.d(Permission.TAG, "shouldShowPermissionsRationale: " + rationalPermissions.size());
                CALLBACK.shouldShowPermissionsRationale(rationalPermissions.toArray(new String[0]));
            } else {
                Log.d(Permission.TAG, "onPermissionsRejected: " + rejectedPermissions.size());
                CALLBACK.onPermissionsRejected(rejectedPermissions.toArray(new String[0]));
            }
        }
        finish();
    }
}

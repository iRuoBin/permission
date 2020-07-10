package com.iruobin.android.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionActivity extends Activity {

    public static final String KEY_PERMISSIONS = "permissions";
    private static final int RC_REQUEST_PERMISSION = 100;
    private static PermissionCallback sCallback;
    private String[] requestPermissions;

    public static void request(Context context, String[] permissions, PermissionCallback callback) {
        sCallback = callback;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_PERMISSIONS, permissions);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getIntent().hasExtra(KEY_PERMISSIONS)) {
            return;
        }
        // 当api大于23时，才进行权限申请
        requestPermissions = getIntent().getStringArrayExtra(KEY_PERMISSIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(requestPermissions, RC_REQUEST_PERMISSION);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != RC_REQUEST_PERMISSION) {
            finish();
            return;
        }
        // 在某些设备上,当用户拒绝权限时返回空
        if (permissions == null || permissions.length == 0 || grantResults == null || grantResults.length != permissions.length) {
            if (sCallback != null) {
                sCallback.onPermissionsResult(new ArrayList<String>(), new ArrayList<String>(), Arrays.asList(requestPermissions));
            }
            finish();
            return;
        }
        // 处理申请结果
        boolean[] shouldShowRequestPermissionRationale = new boolean[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i]);
        }
        this.onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale);
    }


    @TargetApi(Build.VERSION_CODES.M)
    void onRequestPermissionsResult(String[] permissions, int[] grantResults, boolean[] shouldShowRequestPermissionRationale) {
        List<String> grantedPermissions = new ArrayList<>();
        List<String> rationalPermissions = new ArrayList<>();
        List<String> rejectedPermissions = new ArrayList<>();

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                // 用户同意的权限
                PrintLog.d("Granted: " + permissions[i]);
                grantedPermissions.add(permissions[i]);
            } else {
                if (shouldShowRequestPermissionRationale[i]) {
                    // 用户拒绝的权限（未勾选不再提示）
                    PrintLog.d("Rationale: " + permissions[i]);
                    rationalPermissions.add(permissions[i]);
                } else {
                    // 用户拒绝的权限（勾选不再提示）
                    PrintLog.d("Rejected: " + permissions[i]);
                    rejectedPermissions.add(permissions[i]);
                }
            }
        }

        if (sCallback != null) {
            sCallback.onPermissionsResult(grantedPermissions, rationalPermissions, rejectedPermissions);
        }

        if (permissions.length == grantedPermissions.size()) {
            PrintLog.d("allPermissionsGranted: " + grantedPermissions.size());
            if (sCallback != null) {
                sCallback.allPermissionsGranted(grantedPermissions);
            }
        }
        finish();
    }
}

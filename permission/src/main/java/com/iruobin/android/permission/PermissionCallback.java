package com.iruobin.android.permission;

/*
 * 权限申请回调
 */
public interface PermissionCallback {
    void onPermissionGranted(String permission);
    void onPermissionsGranted(String[] permissions);
    void shouldShowPermissionRationale(String permission);
    void shouldShowPermissionsRationale(String[] permissions);
    void onPermissionRejected(String permission);
    void onPermissionsRejected(String[] permissions);
}

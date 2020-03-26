package com.iruobin.android.permission;

public abstract class PermissionDefaultCallback implements PermissionCallback {

    public abstract void onPermissionsGrant(String[] permissions);
    public abstract void shouldShowRationals(String[] permissions);
    public abstract void onPermissionsReject(String[] permissions);

    @Override
    public void onPermissionGranted(String permission) {}

    @Override
    public void onPermissionsGranted(String[] permissions) {
        onPermissionsGrant(permissions);
    }

    @Override
    public void shouldShowPermissionRationale(String permission) {}

    @Override
    public void shouldShowPermissionsRationale(String[] permissions) {
        shouldShowRationals(permissions);
    }

    @Override
    public void onPermissionRejected(String permission) {}

    @Override
    public void onPermissionsRejected(String[] permissions) {
        onPermissionsReject(permissions);
    }
}

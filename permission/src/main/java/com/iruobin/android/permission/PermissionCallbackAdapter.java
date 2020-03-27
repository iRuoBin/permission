package com.iruobin.android.permission;

public abstract class PermissionCallbackAdapter extends PermissionDefaultCallback {

    public abstract void permissionsGranted(String[] permissions);

    @Override
    public void onPermissionsGrant(String[] permissions) {
        permissionsGranted(permissions);
    }

    @Override
    public void shouldShowRationals(String[] permissions) {

    }

    @Override
    public void onPermissionsReject(String[] permissions) {

    }
}

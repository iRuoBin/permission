package com.iruobin.android.permission;

import android.content.Context;

public class Permission {
    // 权限申请回调
    private PermissionActivity.PermissionCallback callback;
    // 需要申请的权限
    private String[] permissions;
    private Context context;

    private Permission(Context context) {
        this.context = context;
    }

    public static Permission with(Context context) {
        return new Permission(context);
    }

    public Permission permission(String[] permissions) {
        this.permissions = permissions;
        return this;
    }

    public Permission callback(PermissionActivity.PermissionCallback callback) {
        this.callback = callback;
        return this;
    }

    public void request() {
        if (permissions == null || permissions.length <= 0) {
            return;
        }
        PermissionActivity.request(context, permissions, callback);
    }
}

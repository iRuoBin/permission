package com.iruobin.android.permission;

import android.content.Context;
import android.util.Log;

public class Permission {
    private Context context;
    // 需要申请的权限
    private String[] permissions;
    // 权限申请回调
    private PermissionCallback callback;

    private Permission(Context context) {
        this.context = context;
        PrintLog.init(context);
    }

    public static Permission with(Context context) {
        return new Permission(context);
    }

    public Permission permission(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    public Permission callback(PermissionCallback callback) {
        this.callback = callback;
        return this;
    }

    public void request() {
        if (permissions == null || permissions.length == 0) {
            PrintLog.d("request permissions is null");
            return;
        }
        PermissionActivity.request(context, permissions, callback);
    }
}

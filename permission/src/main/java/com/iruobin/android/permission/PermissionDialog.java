package com.iruobin.android.permission;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

/**
 * 权限引导弹框（权限用途说明/设置中心引导）
 */
public class PermissionDialog {

    public static final int RATIONALE = 1;
    public static final int SETTING = 2;

    public static final int DEF_REQUEST_CODE = -1024;

    public static AlertDialog rational(Context context, String description,
                                       PermissionCallback permissionCallback,
                                       String... permissions) {

        return createDialog(context, description, RATIONALE, DEF_REQUEST_CODE, permissionCallback, permissions);
    }

    public static AlertDialog setting(Context context, String description) {
        return createDialog(context, description, SETTING, DEF_REQUEST_CODE, null);
    }

    /**
     * 设置 request code 可以在 onActivityResult 中再次处理权限问题 (比如判断是否勾选了所需权限)
     */
    public static AlertDialog setting(Context context, String description, int requestCode) {
        return createDialog(context, description, SETTING, requestCode, null);
    }

    private static AlertDialog createDialog(final Context context, final String description, final int type, final int requestCode,
                                            final PermissionCallback callback, final String... permissions) {
        return new AlertDialog.Builder(context)
                .setMessage(description)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == RATIONALE) {
                            Permission.with(context).permission(permissions).callback(callback).request();
                        } else if (type == SETTING) {
                            if (context instanceof Activity) {
                                //跳转至设置中心
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                intent.setData(uri);
                                //设置 requestCode（大于0）可以在 onActivityResult 中再次判断是否勾选了所需权限
                                ((Activity) context).startActivityForResult(intent, requestCode);
                            }
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .create();
    }
}

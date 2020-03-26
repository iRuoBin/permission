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

    private enum Type {
        /**
         * 权限用途说明提示框
         */
        RATIONALE,

        /**
         * 去设置中心引导弹框
         */
        SETTING
    }

    Type mType;
    Context mContext;
    String mDescription;

    String[] mPermissions;
    PermissionCallback mPermissionCallback;

    int mRequestCode;

    private PermissionDialog(Type type, Context context, String description) {
        mType = type;
        mContext = context;
        mDescription = description;
    }

    private PermissionDialog setPermissions(String[] permissions) {
        mPermissions = permissions;
        return this;
    }

    private PermissionDialog setPermissionCallback(PermissionCallback permissionCallback) {
        mPermissionCallback = permissionCallback;
        return this;
    }

    private PermissionDialog setRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return this;
    }

    public static AlertDialog rational(Context context, String description,
                                       PermissionCallback permissionCallback,
                                       String... permissions) {

        return new PermissionDialog(Type.RATIONALE, context, description)
                .setPermissions(permissions)
                .setPermissionCallback(permissionCallback)
                .createDialog();
    }

    public static AlertDialog setting(Context context, String description) {
        return new PermissionDialog(Type.SETTING, context, description).createDialog();
    }

    /**
     * 设置 request code 可以在 onActivityResult 中再次处理权限问题 (比如判断是否勾选了所需权限)
     */
    public static AlertDialog setting(Context context, String description, int requestCode) {
        return new PermissionDialog(Type.SETTING, context, description)
                .setRequestCode(requestCode)
                .createDialog();
    }

    private AlertDialog createDialog() {
        return new AlertDialog.Builder(mContext)
                .setMessage(mDescription)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mType == Type.RATIONALE) {
                            Permission.with(mContext).permission(mPermissions)
                                    .callback(mPermissionCallback).request();
                        } else if (mType == Type.SETTING) {
                            if (mContext instanceof Activity) {
                                //跳转至设置中心
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                                intent.setData(uri);
                                //设置 request code 可以在 onActivityResult 中再次判断是否勾选了所需权限
                                ((Activity) mContext).startActivityForResult(intent, mRequestCode);
                            }
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        if (mContext instanceof Activity) {
//                            ((Activity) mContext).finish();
//                        }
                    }
                })
                .create();
    }
}

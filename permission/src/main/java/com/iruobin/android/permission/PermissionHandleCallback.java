package com.iruobin.android.permission;

import android.content.Context;

public abstract class PermissionHandleCallback extends PermissionDefaultCallback {

    Context mContext;
    String mRationale;
    String mSetting;

    public PermissionHandleCallback(Context context) {
        mContext = context;
    }

    public PermissionHandleCallback(Context context, String rationale, String setting) {
        mContext = context;
        mRationale = rationale;
        mSetting = setting;
    }

    public abstract void onPermissionsCompleteGranted();

    @Override
    public void onPermissionsGrant(String[] permissions) {
        onPermissionsCompleteGranted();
    }

    @Override
    public void shouldShowRationals(String[] permissions) {
        String description = mRationale;
        if (description == null || description.isEmpty()) {
            description = "该功能需要以下权限才可正常使用:\n\n" + permissionsDescription(permissions);
        }
        PermissionDialog.rational(mContext, description, this, permissions).show();
    }

    @Override
    public void onPermissionsReject(String[] permissions) {
        String description = mSetting;
        if (description == null || description.isEmpty()) {
            description = "该功能需要以下权限才可正常使用:\n\n" + permissionsDescription(permissions)
                    + "\n\n请前往设置手动开启";
        }
        PermissionDialog.setting(mContext, description).show();
    }

    private String permissionsDescription(String[] permissions) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String permission : permissions) {
            stringBuilder.append(engToCh(permission.substring(permission.lastIndexOf(".") + 1)));
            stringBuilder.append(", ");
        }
        if (stringBuilder.length() > 2) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }

        return stringBuilder.toString();
    }

    private String engToCh(String description) {
        if ("CAMERA".equals(description)) {
            return description.replace("CAMERA", "相机");
        } else if ("RECORD_AUDIO".equals(description)) {
            return description.replace("RECORD_AUDIO", "录音");
        } else if ("READ_CALENDAR".equals(description)) {
            return description.replace("READ_CALENDAR", "读取日历");
        } else if ("WRITE_CALENDAR".equals(description)) {
            return description.replace("WRITE_CALENDAR", "添加日历");
        } else if ("READ_CONTACTS".equals(description)) {
            return description.replace("READ_CONTACTS", "读取联系人");
        } else if ("WRITE_CONTACTS".equals(description)) {
            return description.replace("WRITE_CONTACTS", "添加联系人");
        } else if ("READ_EXTERNAL_STORAGE".equals(description)) {
            return description.replace("READ_EXTERNAL_STORAGE", "读取手机存储");
        } else if ("WRITE_EXTERNAL_STORAGE".equals(description)) {
            return description.replace("WRITE_EXTERNAL_STORAGE", "写入手机存储");
        } else if ("READ_SMS".equals(description)) {
            return description.replace("READ_SMS", "读取短信");
        } else if ("SEND_SMS".equals(description)) {
            return description.replace("SEND_SMS", "发送短信");
        } else if ("READ_PHONE_STATE".equals(description)) {
            return description.replace("READ_PHONE_STATE", "获取手机信息");
        } else {
            return description;
        }
    }

}
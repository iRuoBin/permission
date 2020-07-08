package com.iruobin.android.permission;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PermissionHandleCallback implements PermissionCallback {

    private Context mContext;
    private String mRationaleDescription;
    private String mSettingDescription;
    private int mRequestCode = PermissionDialog.DEF_REQUEST_CODE;
    public Map<Object, String> mDefaultDescriptions = new HashMap<>();

    public PermissionHandleCallback(Context context) {
        init(context, "", "", mRequestCode);
    }

    public PermissionHandleCallback(Context context, int requestCode) {
        init(context, "", "", requestCode);
    }

    public PermissionHandleCallback(Context context, String rationaleDescription,
                                    String settingDescription, int requestCode) {
        init(context, rationaleDescription, settingDescription, requestCode);
    }

    private void init(Context context, String rationaleDescription,
                      String settingDescription, int requestCode) {
        mContext = context;
        mRationaleDescription = rationaleDescription;
        mSettingDescription = settingDescription;
        mRequestCode = requestCode;
        initDefaultDescription();
    }

    public abstract void onPermissionsCompleteGranted(List<String> grantedPermissions);

    @Override
    public void allPermissionsGranted(List<String> grantedPermissions) {
        onPermissionsCompleteGranted(grantedPermissions);
    }

    @Override
    public void onPermissionsResult(List<String> grantedPermissions, List<String> rationalePermissions,
                                    List<String> rejectedPermissions) {
        if (rationalePermissions.size() > 0) {
            if (TextUtils.isEmpty(mRationaleDescription)) {
                mRationaleDescription = String.format(mDefaultDescriptions.get(PermissionDialog.RATIONALE),
                        permissionsDescription(rationalePermissions));
            }
            PermissionDialog.rational(mContext, mRationaleDescription, this,
                    rationalePermissions.toArray(new String[0])).show();
        } else if (rejectedPermissions.size() > 0) {
            if (TextUtils.isEmpty(mSettingDescription)) {
                mSettingDescription = String.format(mDefaultDescriptions.get(PermissionDialog.SETTING),
                        permissionsDescription(rejectedPermissions));
            }
            PermissionDialog.setting(mContext, mSettingDescription, mRequestCode).show();
        }
    }

    private String permissionsDescription(List<String> permissions) {
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

    private void initDefaultDescription() {
        mDefaultDescriptions.put(PermissionDialog.RATIONALE, "该功能需要以下权限才可正常使用:\n\n %s");
        mDefaultDescriptions.put(PermissionDialog.SETTING, "该功能需要以下权限才可正常使用:\n\n %s \n\n请前往设置手动开启");
        mDefaultDescriptions.put("CAMERA", "相机");
        mDefaultDescriptions.put("RECORD_AUDIO", "录音");
        mDefaultDescriptions.put("READ_CALENDAR", "读取日历");
        mDefaultDescriptions.put("WRITE_CALENDAR", "添加日历");
        mDefaultDescriptions.put("READ_CONTACTS", "读取联系人");
        mDefaultDescriptions.put("WRITE_CONTACTS", "添加联系人");
        mDefaultDescriptions.put("READ_EXTERNAL_STORAGE", "读取手机存储");
        mDefaultDescriptions.put("WRITE_EXTERNAL_STORAGE", "写入手机存储");
        mDefaultDescriptions.put("READ_SMS", "读取短信");
        mDefaultDescriptions.put("SEND_SMS", "发送短信");
        mDefaultDescriptions.put("READ_PHONE_STATE", "获取手机信息");
        mDefaultDescriptions.put("ACCESS_FINE_LOCATION", "获取精确定位");
        mDefaultDescriptions.put("ACCESS_COARSE_LOCATION", "获取定位信息");
        mDefaultDescriptions.put("CALL_PHONE", "拨打电话");
        mDefaultDescriptions.put("CHANGE_WIFI_STATE", "修改Wifi状态");
        mDefaultDescriptions.put("CHANGE_NETWORK_STATE", "修改网络状态");
        mDefaultDescriptions.put("READ_CALL_LOG", "读取通话记录");
    }

    private String engToCh(String description) {
        if (mDefaultDescriptions.containsKey(description)) {
            return mDefaultDescriptions.get(description);
        } else {
            return description;
        }
    }

}

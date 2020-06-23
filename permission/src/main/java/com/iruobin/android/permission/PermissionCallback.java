package com.iruobin.android.permission;

import java.util.List;

/*
 * 权限申请回调
 *
 * 如果动态申请的权限在 Manifest 文件中未注册 会走 rejected Permission
 */
public interface PermissionCallback {

    /**
     * 用户同意所有权限
     */
    void allPermissionsGranted(List<String> grantedPermissions);

    /**
     * 用户操作完本次申请全部权限时
     * @param grantedPermissions 返回 已授权的权限
     * @param rationalePermissions 返回 拒绝的权限（但未勾选不再提示）
     * @param rejectedPermissions 返回 拒绝的权限（勾选不再提示）
     */
    void onPermissionsResult(List<String> grantedPermissions, List<String> rationalePermissions, List<String> rejectedPermissions);

}

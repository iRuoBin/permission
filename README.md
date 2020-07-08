# permission
**Android 动态权限申请库**

使用方法：
项目根目录 build.gradle 添加
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

使用该库的 module 的 build.gradle 添加
```groovy
dependencies {
    implementation 'com.github.iRuoBin:permission:Version'
}
```
最新版本 [![](https://jitpack.io/v/iRuoBin/permission.svg)](https://jitpack.io/#iRuoBin/permission)
</br>
> **注意**：申请的权限必须在 Manifest 里列出

```java
Permission.with(context)
        .permission(Manifest.permission.CAMERA ...) //这里写需要的多个权限
        .callback(new PermissionCallback() {
            /**
             * 所有权限都同意后调用
             */
            @Override
            public void allPermissionsGranted(List<String> grantedPermissions) {
                
            }

            /**
             * 权限的所有操作状态（总会调用）
             * @param grantedPermissions 所有同意的权限
             * @param rationalePermissions 所有拒绝但是未勾选不再询问的权限
             * @param rejectedPermissions 所有拒绝但勾选不再询问的权限
             */
            @Override
            public void onPermissionsResult(List<String> grantedPermissions, 
                                            List<String> rationalePermissions, 
                                            List<String> rejectedPermissions) {
                
            }
        })
        .request();
```
> **注意**：Android 6.0 以下的手机会同意所有权限

使用默认处理 callback 的用法：
```java
Permission.with(context)
        .permission(Manifest.permission.CAMERA ...) //这里写需要的多个权限
        .callback(new PermissionHandleCallback(context) { //该callback有多个构造函数，可以设定弹框文案
            /**
             * 所有权限都同意后调用
             */
            @Override
            public void onPermissionsCompleteGranted(List<String> grantedPermissions) {

            }
        })
        .request();
```
使用默认处理 callback 时 -> 用户**拒绝但是未勾选不再询问选项**时（rational），会弹出如下弹框（文案可设定）：

![rational](/screenshots/rational.png)</br>

点 **确定按钮** 会再次请求权限，点 **取消按钮** 则弹框消失
</br></br></br>
使用默认处理 callback 时 -> 用户**拒绝且勾选不再询问选项**时（setting），会弹出如下弹框（文案可设定）：

![setting](/screenshots/setting.png)</br>

点 **确定按钮** 会进入该应用的设置界面，引导用户手动开启权限，点 **取消按钮** 则弹框消失


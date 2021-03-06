package com.example.ipermission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.iruobin.android.permission.Permission
import com.iruobin.android.permission.PermissionHandleCallback
import com.iruobin.android.permission.PrintLog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_1.setOnClickListener{ test() }
    }

    fun test() {
        Permission.with(this).permission(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
            , Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
            , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS, Manifest.permission.WRITE_CONTACTS)
            .callback(object : PermissionHandleCallback(this, 5) {
                override fun onPermissionsCompleteGranted(grantedPermissions: MutableList<String>?) {
                    PrintLog.d("all permission is granted")
                }
            }).request()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        PrintLog.d("requestCode:$requestCode, resultCode:$resultCode")
    }
}

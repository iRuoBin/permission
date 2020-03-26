package com.example.ipermission

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.iruobin.android.permission.Permission
import com.iruobin.android.permission.PermissionHandleCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_1.setOnClickListener{ test() }
    }

    fun test() {
        Permission.with(this).permission(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CONTACTS
            , Manifest.permission.SEND_SMS, Manifest.permission.CAMERA)
            .callback(object : PermissionHandleCallback(this){
                override fun onPermissionsCompleteGranted() {
                    Log.d("robin", "onPermissionsGranted")
                }
            } ).request()
    }

}

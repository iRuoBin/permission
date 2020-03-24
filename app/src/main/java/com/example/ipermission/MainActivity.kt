package com.example.ipermission

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.iruobin.android.permission.Permission
import com.iruobin.android.permission.PermissionActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_1.setOnClickListener{ test() }
//        test()
    }

    fun test() {
        val strings = arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CONTACTS
            , Manifest.permission.SEND_SMS, Manifest.permission.CAMERA)
        Permission.with(this).permission(strings)
            .callback(object : PermissionActivity.PermissionCallback {
                override fun onPermissionGranted() {
                    Log.d("robin", "onPermissionGranted")
                }
                override fun shouldShowRational(permission: String) {
                    Log.d("robin", "shouldShowRational " + permission)
                }
                override fun onPermissionReject(permission: String) {
                    Log.d("robin", "onPermissionReject " + permission)
                }
            }).request()
    }

}

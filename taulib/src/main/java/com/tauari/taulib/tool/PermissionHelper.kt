package com.tauari.taulib.tool

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tauari.taulib.RequestCode

object PermissionHelper {
    const val tag = "PermissionHelper"

    fun checkWritingPermission(activity: Activity) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            if(isReadExternalStorageGranted(activity.applicationContext) && isWriteExternalStorageGranted(activity.applicationContext)) {
                Log.e(tag, "Permission granted before")
            }
            else {
                Log.e(tag, "request Permission")
                requestPermissions(activity)
            }
    }

    private fun isReadExternalStorageGranted(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(context,
            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
    }
    private fun isWriteExternalStorageGranted(context: Context): Boolean {
        return if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                    == PackageManager.PERMISSION_GRANTED)
        } else {
            true
        }
    }

    private fun requestPermissions(activity: Activity) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                RequestCode.PERMISSION_READ_AND_WRITE_EXTERNAL)
        }
        else {
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                RequestCode.PERMISSION_READ_EXTERNAL)
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray, callback: PermissionsResultCallback) {
        if (requestCode == RequestCode.PERMISSION_READ_AND_WRITE_EXTERNAL) {
            if(grantResults.isEmpty()) {
                callback.onFailed()
            }
            else {
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    callback.onFailed()
                }
                if(grantResults.size == 2 && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    callback.onFailed()
                }
            }
        }
        else if(requestCode == RequestCode.PERMISSION_READ_EXTERNAL) {
            if(grantResults.isEmpty()) {
                callback.onFailed()
            }
            else {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    callback.onFailed()
                }
            }
        }
    }

    interface PermissionsResultCallback {
        fun onFailed()
    }
}
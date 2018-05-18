package co.netguru.blueprintlibrary.common.permissions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import co.netguru.blueprintlibrary.common.Constants


object PermissionsHelper {

    fun onRequestPermissionsResult(activity: FragmentActivity, callback: PermissionCallBack?, requestCode: Int, permissions: Array<String>,
                                   grantResults: IntArray) {
        if (requestCode == Constants.REQUEST_PERMISSION) {
            when {
                !grantResults.isEmpty()
                        && grantResults[Constants.PERMISSION_RESULT_INDEX] ==
                        PackageManager.PERMISSION_GRANTED -> {
                    callback?.permissionGranted()
                }


                else -> callback?.permissionDenied()
            }
        } else {
            activity.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun checkPermission(permission: String, activity: FragmentActivity?) =
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ContextCompat.checkSelfPermission(activity!!, permission) ==
                        PackageManager.PERMISSION_GRANTED
            } else true

    fun checkGpsStatusOn(activity: FragmentActivity, locationSourceSettingsCode: Int,
                         goToSettings: Boolean): Boolean {
        val gpsOn = isGpsOn(activity)
        if (!gpsOn && goToSettings) {
            activity.startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                    locationSourceSettingsCode)
        }
        return gpsOn
    }

    private fun isGpsOn(context: Context?): Boolean {
        return try {
            Settings.Secure.getInt(context!!.contentResolver,
                    Settings.Secure.LOCATION_MODE) != Settings.Secure.LOCATION_MODE_OFF
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun hasCalendarPermissions(context: Context?): Boolean {
        return ContextCompat.checkSelfPermission(context!!,
                Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
    }

    fun hasStoragePermissions(context: Context) =
            ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

}
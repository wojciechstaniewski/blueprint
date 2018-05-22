package co.netguru.blueprintlibrary.common.permissions

import android.Manifest
import android.support.v4.app.ActivityCompat
import co.netguru.blueprintlibrary.common.Constants
import co.netguru.blueprintlibrary.common.view.ErrorHandlingActivity


abstract class PermissionsActivity : ErrorHandlingActivity() {

    private var callback: PermissionCallBack? = null

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        PermissionsHelper.onRequestPermissionsResult(this, callback, requestCode, permissions, grantResults)
    }

    fun requestPermissions(permission: String, permissionCallback: PermissionCallBack?) {
        callback = permissionCallback
        ActivityCompat.requestPermissions(this, arrayOf(permission), Constants.REQUEST_PERMISSION)
    }

    fun requestWriteCalendarPermissions(callback: PermissionCallBack) {
        requestPermissions(Manifest.permission.WRITE_CALENDAR, callback)
    }
}

package co.netguru.blueprintlibrary.common.utils

import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.support.v4.app.Fragment


object CameraUtils {

    fun openCameraApp(packageManager: PackageManager, fragment: Fragment,
                      requestCameraCapture: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            fragment.startActivityForResult(takePictureIntent, requestCameraCapture)
        }
    }

}
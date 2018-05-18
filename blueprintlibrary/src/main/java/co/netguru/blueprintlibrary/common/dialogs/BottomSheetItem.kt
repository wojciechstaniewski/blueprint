package co.netguru.blueprintlibrary.common.dialogs

import android.view.View
import co.netguru.blueprintlibrary.R

enum class BottomSheetItem constructor(var iconResId: Int, var titleResId: Int) {

    CAMERA_ITEM(R.drawable.ic_photo_camera, R.string.camera),
    CHOOSE_PHOTO_ITEM(R.drawable.ic_menu_gallery, R.string.choose_photo),
    ATTACH_FILE_ITEM(R.drawable.ic_attach_file, R.string.choose_file),
    GOOGLE_DRIVE_ITEM(R.drawable.ic_google_drive, R.string.google_drive);

    lateinit var clickListener: View.OnClickListener

    fun setOnClickListener(clickListener: View.OnClickListener) {
        this.clickListener = clickListener
    }
}
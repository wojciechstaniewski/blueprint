package co.netguru.blueprintlibrary.common.customViews

import android.graphics.drawable.Drawable

internal interface CustomizableByCode {
    fun setSpinningBarWidth(width: Float)
    fun setSpinningBarColor(color: Int)
    fun setAnimationTextColor(color: Int)
    fun setDoneColor(color: Int)
    fun setPaddingProgress(padding: Float)
    fun setInitialHeight(height: Int)
    fun setInitialCornerRadius(radius: Float)
    fun setFinalCornerRadius(radius: Float)
    fun setAnimationBackground(background: Drawable)
}

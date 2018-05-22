package co.netguru.blueprintlibrary.common.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.*

object DisplayUtils {

    fun setSendDrawable(send: Drawable, mic: Drawable, speech: Boolean): Drawable {
        if (speech) {
            return mic
        } else {
            return send
        }
    }

    fun setDrawableWithTint(drawable: Drawable, color: Int): Drawable {
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        return drawable
    }

    fun getDisplaySize(context: Context): Float {
        val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val x = Math.pow(dm.widthPixels.toDouble(), 2.0)
        val navH = getNavBarHeight(context)
        val h = dm.heightPixels + navH
        val y = Math.pow(h.toDouble(), 2.0)

        val factor: Float = if (h > 1280) {
            dm.xdpi
        } else {
            if (navH > 0) {
                dm.densityDpi.toFloat()
            } else {
                dm.xdpi
            }
        }

        return (Math.sqrt(x + y) / factor).toFloat()
    }

    fun getNavBarHeight(c: Context): Int {
        val result = 0
        val hasMenuKey = ViewConfiguration.get(c).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)

        if (!hasMenuKey && !hasBackKey) {
            //The device has a navigation bar
            val resources = c.resources

            val orientation = resources.configuration.orientation
            val resourceId: Int
            if (isTablet(c)) {
                resourceId = resources.getIdentifier(if (orientation == Configuration.ORIENTATION_PORTRAIT) "navigation_bar_height" else "navigation_bar_height_landscape", "dimen", "android")
            } else {
                resourceId = resources.getIdentifier(if (orientation == Configuration.ORIENTATION_PORTRAIT) "navigation_bar_height" else "navigation_bar_width", "dimen", "android")
            }

            if (resourceId > 0) {
                return resources.getDimensionPixelSize(resourceId)
            }
        }
        return result
    }

    private fun isTablet(c: Context): Boolean {
        return c.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    fun isLargeScreen(view: View): Boolean {
        return getDisplaySize(view.context) > 4.5
    }

    private fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    private fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

}
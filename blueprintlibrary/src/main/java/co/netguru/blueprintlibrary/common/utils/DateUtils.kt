package co.netguru.blueprintlibrary.common.utils


import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val dateFormat = SimpleDateFormat("d MMM yyyy",Locale.US)
    private val timeFormat = SimpleDateFormat("K:mma",Locale.US)

    val currentTime: String
        get() {

            val today = Calendar.getInstance().time
            return timeFormat.format(today)
        }

    val currentDate: String
        get() {

            val today = Calendar.getInstance().time
            return dateFormat.format(today)
        }

}

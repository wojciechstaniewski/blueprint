package co.netguru.blueprintlibrary.common.utils

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.style.BackgroundColorSpan
import co.netguru.blueprintlibrary.R
import co.netguru.blueprintlibrary.common.Constants


object TextUtils {

    fun setSpanForSearch(chatMessage: String?, searchString: String, context: Context): Spannable? {
        val spannable: Spannable?
        if (chatMessage != null && chatMessage.contains(searchString, true)) {
            val startPos = chatMessage.indexOf(searchString,
                    Constants.STRING_FIRST_OCCURENCE_DEFAULT_INDEX, true)
            val endPos = startPos + searchString.length
            spannable = Spannable.Factory.getInstance().newSpannable(chatMessage)
            if (spannable.length > 0 && startPos <= spannable.length && endPos <= spannable.length) {
                spannable.setSpan(BackgroundColorSpan(
                        ContextCompat.getColor(context, R.color.confirmation_active)),
                        startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        } else {
            spannable = null
        }
        return spannable
    }

}
package co.netguru.blueprintlibrary.common.view

import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatButton
import android.text.InputType
import co.netguru.blueprintlibrary.R
import co.netguru.blueprintlibrary.common.utils.ValidatorUtils

abstract class BaseFormFragment : Fragment(), ValidatorUtils.InputFieldValidationListener {

    abstract override fun afterTexChanged(textInputLayout: TextInputLayout)

    var fields: MutableList<TextInputLayout> = arrayListOf()

    lateinit var submitButton: AppCompatButton

    fun setItemsToValidation() {
        ValidatorUtils(fields, this)
    }

    override fun valid(textInputLayout: TextInputLayout) {
        textInputLayout.error = null
    }

    override fun allFieldsAreValid() {
        setSubmitButton(submitButton, true)
    }

    override fun error(textInputLayout: TextInputLayout) {
        setSubmitButton(submitButton, false)
        when (textInputLayout.editText!!.inputType - InputType.TYPE_CLASS_TEXT) {
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> textInputLayout.error = getString(R.string.invalid_email)
            InputType.TYPE_TEXT_VARIATION_PASSWORD -> textInputLayout.error = getString(R.string.invalid_password)
        }
    }

    private fun setSubmitButton(button: AppCompatButton, enabled: Boolean) {
        button.background = if (enabled) {
            ContextCompat.getDrawable(context!!, R.drawable.shape_white)
        } else {
            ContextCompat.getDrawable(context!!, R.drawable.shape_blue)
        }
        button.setTextColor(if (enabled) {
            ContextCompat.getColor(context!!, R.color.colorPrimary)
        } else {
            ContextCompat.getColor(context!!, R.color.backgroundColor)
        })
        button.isEnabled = enabled
        button.isClickable = enabled
    }

}
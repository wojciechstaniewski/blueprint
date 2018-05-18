package co.netguru.blueprintlibrary.common.utils

import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Patterns
import java.util.regex.Pattern


class ValidatorUtils constructor(private val fields: MutableList<TextInputLayout>,
                                 private val inputFieldValidationListener: InputFieldValidationListener) {

    init {
        for (field: TextInputLayout in fields) {
            validateInputField(field)
        }
    }

    private fun validateInputField(textInputLayout: TextInputLayout) {
        textInputLayout.editText!!.addTextChangedListener(object : TextWatcher {
            var isValid: Boolean = false

            override fun afterTextChanged(s: Editable) {
                when (textInputLayout.editText!!.inputType) {
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS + InputType.TYPE_CLASS_TEXT -> {
                        isValid = isValidEmail(s.isNotBlank(), textInputLayout.editText!!.text.toString().trim())
                    }
                    InputType.TYPE_TEXT_VARIATION_PASSWORD + InputType.TYPE_CLASS_TEXT -> {
                        isValid = isInputPasswordValid(s.isNotBlank(), textInputLayout)
                    }
                    InputType.TYPE_CLASS_PHONE, InputType.TYPE_CLASS_TEXT -> {
                        isValid = s.isNotBlank()
                    }
                }
                setInputFieldValidationListener(isValid = isValid, textInputLayout = textInputLayout, inputFieldValidationListener = inputFieldValidationListener)
                inputFieldValidationListener.afterTexChanged(textInputLayout)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // other stuffs
            }

            override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // other stuffs
            }
        })
    }

    private fun setInputFieldValidationListener(isValid: Boolean, textInputLayout: TextInputLayout,
                                                inputFieldValidationListener: InputFieldValidationListener) =
            if (isValid) {
                handleValidFields(textInputLayout)
            } else {
                inputFieldValidationListener.error(textInputLayout)
            }

    private fun isInputPasswordValid(isNotBlank: Boolean, textInputLayout: TextInputLayout): Boolean =
            if (isNotBlank) {
                isPasswordValid(textInputLayout.editText!!.text.toString().trim())
            } else {
                true
            }

    private fun handleValidFields(textInputLayout: TextInputLayout) {
        inputFieldValidationListener.valid(textInputLayout)
        if (checkIfOtherFieldsAreValid()) {
            inputFieldValidationListener.allFieldsAreValid()
        }
    }

    private fun isValidEmail(isNotBlank: Boolean, target: CharSequence): Boolean = if (isNotBlank) {
        Patterns.EMAIL_ADDRESS.matcher(target).matches()
    } else {
        true
    }

    private fun isPasswordValid(password: CharSequence?): Boolean {
        return Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$").matcher(password).matches()
    }

    private fun matchReInputPasswordWithPassword(rePassword: CharSequence,
                                                 password: CharSequence?): Boolean {
        return rePassword == password
    }


    private fun checkIfOtherFieldsAreValid(): Boolean {
        var result = true
        for (field: TextInputLayout in fields) {
            result = result && field.error.isNullOrEmpty() && field.editText!!.text.isNotEmpty()
        }
        return result
    }

    interface InputFieldValidationListener {
        fun valid(textInputLayout: TextInputLayout)
        fun allFieldsAreValid()
        fun error(textInputLayout: TextInputLayout)
        fun afterTexChanged(textInputLayout: TextInputLayout)
    }

}
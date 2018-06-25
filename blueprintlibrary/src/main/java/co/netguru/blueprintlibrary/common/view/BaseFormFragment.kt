package co.netguru.blueprintlibrary.common.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatButton
import android.text.InputType
import co.netguru.blueprintlibrary.R
import co.netguru.blueprintlibrary.common.Repository
import co.netguru.blueprintlibrary.common.utils.ValidatorUtils
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseFormFragment : Fragment(), ValidatorUtils.InputFieldValidationListener {

    @Inject
    lateinit var repository: Repository

    val compositeDisposable = CompositeDisposable()

    abstract override fun afterTexChanged(textInputLayout: TextInputLayout)

    var fields: MutableList<TextInputLayout> = arrayListOf()

    var submitButton: AppCompatButton? = null

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleNetworkConnectivityEvent()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun setItemsToValidation() {
        ValidatorUtils(fields, this)
    }

    override fun valid(textInputLayout: TextInputLayout) {
        textInputLayout.error = null
    }

    private fun handleNetworkConnectivityEvent() {
        compositeDisposable.add(repository.networkConnectivityEvent.getSuccessStream().subscribe {
            if (submitButton != null) {
                setItemsToValidation()
            }
        })

        compositeDisposable.add(repository.networkConnectivityEvent.getErrorStream().subscribe {
            if (submitButton != null) {
                setSubmitButton(submitButton, false)
            }
        })
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

    fun setSubmitButton(button: AppCompatButton?, enabled: Boolean) {
        button?.background = if (enabled) {
            ContextCompat.getDrawable(context!!, R.drawable.shape_green)
        } else {
            ContextCompat.getDrawable(context!!, R.drawable.shape_white)
        }
        button?.setTextColor(if (enabled) {
            ContextCompat.getColor(context!!, R.color.backgroundColor)
        } else {
            ContextCompat.getColor(context!!, R.color.colorPrimary)
        })
        button?.isEnabled = enabled
        button?.isClickable = enabled
    }

}
package co.netguru.blueprint.login.viewmodel

import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.databinding.ObservableField
import co.netguru.blueprint.AppNavigation
import co.netguru.blueprint.login.navigation.LoginNavigationHelper
import co.netguru.blueprintlibrary.common.events.ActionEvent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginRegisterViewModel : ViewModel() {

    var loginUserName: ObservableField<String> = ObservableField()
    var loginPassword: ObservableField<String> = ObservableField()
    var registerUserName: ObservableField<String> = ObservableField()
    var registerPassword: ObservableField<String> = ObservableField()
    var registerRePassword: ObservableField<String> = ObservableField()
    var forgotUserName: ObservableField<String> = ObservableField()

    val loginEvent = ActionEvent<Intent>()
    val registerEvent = ActionEvent<Long>()
    val passwordResetEvent = ActionEvent<Boolean>()
    val passwordStrengthEvent = ActionEvent<Int>()
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var loginNavigationHelper: LoginNavigationHelper

    internal lateinit var successText: String

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun startLogin() {
        loginEvent.onStartProgress()
    }

    fun login() {

    }

    fun startRegister() {
        registerEvent.onStartProgress()
    }

    fun register() {

    }

    fun startReset() {
        passwordResetEvent.onStartProgress()
    }

    fun reset() {

    }

    fun passwordStrength(password: String) {

    }

    fun navigateToSuccessScreen() {
        loginNavigationHelper.navigate(AppNavigation.SUCCESS)
    }

    fun navigateToLogin() {
        loginNavigationHelper.navigate(AppNavigation.LOGIN)
    }

    fun navigateToRegister() {
        loginNavigationHelper.navigate(AppNavigation.REGISTER)
    }

    fun forgotPassword() {
        loginNavigationHelper.navigate(AppNavigation.FORGOT_PASSWORD)
    }

    fun signUp() {
        loginNavigationHelper.navigate(AppNavigation.BACK)
    }
}
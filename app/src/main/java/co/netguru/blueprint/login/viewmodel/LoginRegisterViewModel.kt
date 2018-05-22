package co.netguru.blueprint.login.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import co.netguru.blueprint.AppNavigation
import co.netguru.blueprint.BuildConfig
import co.netguru.blueprint.authenticator.AccountAuthenticator
import co.netguru.blueprint.authenticator.AuthenticationResult
import co.netguru.blueprint.login.navigation.LoginNavigationHelper
import co.netguru.blueprint.services.authentication.AuthenticationManager
import co.netguru.blueprint.services.user.endpoint.UserService
import co.netguru.blueprintlibrary.common.events.ActionEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.security.auth.login.LoginException

class LoginRegisterViewModel : ViewModel() {

    var loginUserName: ObservableField<String> = ObservableField()
    var loginPassword: ObservableField<String> = ObservableField()
    var registerUserName: ObservableField<String> = ObservableField()
    var registerPassword: ObservableField<String> = ObservableField()
    var registerRePassword: ObservableField<String> = ObservableField()
    var forgotUserName: ObservableField<String> = ObservableField()

    val loginEvent = ActionEvent<AuthenticationResult>()
    val registerEvent = ActionEvent<Long>()
    val passwordResetEvent = ActionEvent<Boolean>()
    val passwordStrengthEvent = ActionEvent<Int>()
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var loginNavigationHelper: LoginNavigationHelper

    @Inject
    lateinit var authenticationManager: AuthenticationManager

    internal lateinit var successText: String

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun startLogin() {
        loginEvent.onStartProgress()
    }

    fun login() {
        compositeDisposable.add(authenticationManager.authenticateClient(loginUserName.get()!!.trim(), loginPassword.get()!!.trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.accessToken == null) {
                        loginEvent.onError(LoginException())
                    } else {
                        loginEvent.onSuccess(AuthenticationResult(
                                loginUserName.get()!!.trim(),
                                BuildConfig.APPLICATION_ID,
                                result.accessToken,
                                loginPassword.get()!!.trim(),
                                result.expiresIn?.toString()))
                    }

                }, { e ->
                    Log.e(LoginRegisterViewModel::class.java.simpleName, e.message)
                    loginEvent.onError(e)
                }))
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
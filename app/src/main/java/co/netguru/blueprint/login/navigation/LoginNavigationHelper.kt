package co.netguru.blueprint.login.navigation

import android.support.v4.app.Fragment
import android.util.Log
import co.netguru.blueprint.AppNavigation
import co.netguru.blueprint.R
import co.netguru.blueprint.login.view.*
import javax.inject.Inject

class LoginNavigationHelper @Inject constructor(private var loginActivity: LoginActivity) {

    fun navigate(appNavigation: AppNavigation) {
        when (appNavigation) {
            AppNavigation.LOGIN -> getExistingFragment(loginActivity = loginActivity, fragment = LoginFragmentBuilder.newLoginFragment(loginActivity.getString(R.string.app_name)), appNavigation = appNavigation)
            AppNavigation.REGISTER -> navigateToFragment(loginActivity = loginActivity, fragment = SignInAndRegisterFragmentBuilder.newSignInAndRegisterFragment(loginActivity.getString(R.string.register)), appNavigation = appNavigation)
            AppNavigation.FORGOT_PASSWORD -> navigateToFragment(loginActivity = loginActivity, fragment = ForgotPasswordFragmentBuilder.newForgotPasswordFragment(loginActivity.getString(R.string.forgot_password)), appNavigation = appNavigation)
            AppNavigation.BACK -> back()
            AppNavigation.SUCCESS -> getExistingFragment(loginActivity = loginActivity, fragment = SuccessFragmentBuilder.newSuccessFragment(loginActivity.getString(R.string.success)), appNavigation = appNavigation)
            else -> {
                Log.e("LoginNavigationHelper", "No navigation found !!!")
            }
        }
    }

    private fun back() {
        loginActivity.supportFragmentManager.popBackStack()
    }


    private fun navigateToFragment(loginActivity: LoginActivity, fragment: Fragment, appNavigation: AppNavigation) {
        loginActivity.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, loginActivity.getString(appNavigation.title)).addToBackStack(loginActivity.getString(appNavigation.title)).commit()
    }

    private fun getExistingFragment(loginActivity: LoginActivity, fragment: Fragment, appNavigation: AppNavigation) {
        if (loginActivity.supportFragmentManager.findFragmentByTag(loginActivity.getString(appNavigation.title)) != null) {
            loginActivity.supportFragmentManager.popBackStack(loginActivity.getString(appNavigation.title), 0)
        } else {
            navigateToFragment(loginActivity, fragment, appNavigation)
        }
    }
}
package co.netguru.blueprint.main.navigation

import android.content.Intent
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import co.netguru.blueprint.AppNavigation
import co.netguru.blueprint.R
import co.netguru.blueprint.login.view.SplashScreenFragmentBuilder
import co.netguru.blueprint.login.view.WelcomeScreenFragmentBuilder
import co.netguru.blueprint.main.view.MainActivity
import javax.inject.Inject

class MainNavigationHelper @Inject constructor(private var mainActivity: MainActivity) {


    fun navigate(appNavigation: AppNavigation) {
        when (appNavigation) {
            AppNavigation.SPLASH_SCREEN -> navigateToFragment(mainActivity = mainActivity, fragment = SplashScreenFragmentBuilder.newSplashScreenFragment(mainActivity.getString(appNavigation.title)), tag = mainActivity.getString(R.string.splash_screen))
            AppNavigation.WELCOME_SCREEN -> navigateToFragment(mainActivity = mainActivity, fragment = WelcomeScreenFragmentBuilder.newWelcomeScreenFragment(mainActivity.getString(appNavigation.title)), tag = mainActivity.getString(R.string.welcome_screen))
            AppNavigation.LOGOUT -> mainActivity.logout()
            else -> {
                Log.e("LoginNavigationHelper", "No navigation found !!!")
            }
        }
    }


    fun navigate(intent: Intent) {
        if (intent.resolveActivity(mainActivity.packageManager) != null) {
            mainActivity.startActivity(intent)
        } else {
            Toast.makeText(mainActivity, mainActivity.getString(R.string.no_share_application), Toast.LENGTH_LONG).show()
        }
    }

    private fun navigateToFragment(mainActivity: MainActivity, fragment: Fragment, tag: String) {
        mainActivity.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, tag).commitAllowingStateLoss()
    }

    fun removeAllFragments() {
        for (fragment in mainActivity.supportFragmentManager.fragments) {
            mainActivity.supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
        }
    }
}
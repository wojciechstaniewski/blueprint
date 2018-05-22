package co.netguru.blueprint

import co.netguru.blueprint.R.string.*

enum class AppNavigation(val menuId: Int, val title: Int) {

    LOGIN(app_name, app_name),
    MAIN(app_name, app_name),
    SPLASH_SCREEN(splash_screen, splash_screen),
    WELCOME_SCREEN(welcome_screen, welcome_screen),
    PETS(pets_screen,pets_screen),
    REGISTER(register, register),
    FORGOT_PASSWORD(forgot_password, forgot_password),
    BACK(back, back),
    SUCCESS(success, success),
    LOGOUT(nav_logout, nav_logout);

    companion object {
        fun fromItemId(findValue: Int): AppNavigation = AppNavigation.values().first { it.menuId == findValue }
    }
}
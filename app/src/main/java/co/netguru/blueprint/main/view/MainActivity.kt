package co.netguru.blueprint.main.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.netguru.blueprint.AppNavigation
import co.netguru.blueprint.BR
import co.netguru.blueprint.R
import co.netguru.blueprint.databinding.ActivityMainBinding
import co.netguru.blueprint.main.dependency.MainViewModelSubComponent
import co.netguru.blueprint.main.navigation.MainNavigationHelper
import co.netguru.blueprint.main.viewmodel.MainViewModel
import co.netguru.blueprintlibrary.common.utils.HttpStatus
import co.netguru.blueprintlibrary.common.view.BaseActivity
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Provider

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main, MainViewModel::class.java) {


    override fun setVariables() {
        baseBinding.setVariable(BR.viewModel, baseViewModel)
    }

    @Inject
    lateinit var mainNavigationHelper: MainNavigationHelper

    @Inject
    lateinit var mainViewModelComponent: Provider<MainViewModelSubComponent.Builder>

    companion object {

        fun start(context: Context, bundle: Bundle? = null) {
            val intent = Intent(context, MainActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModelComponent.get().build().inject(baseViewModel)

        if (savedInstanceState == null) {
            mainNavigationHelper.navigate(AppNavigation.SPLASH_SCREEN)
        }

        handleLogoutEvent()
    }


    private fun handleLogoutEvent() {
/*        repository.logoutEvent.getErrorStream().subscribe({
            mainNavigationHelper.removeAllFragments()
            handleError(it)
            mainNavigationHelper.getChatFragment()?.logout()
        })*/
    }

    fun logout() {
        val responseBody: ResponseBody = ResponseBody.create(MediaType.parse("text/plain"), "logout")
        val response: Response<String> = Response.error(HttpStatus.UNAUTHORIZED.value(), responseBody)
        repository.logoutEvent.onError(HttpException(response))
    }

    private fun showLogoutConfirmationDialog() {
        /*    dialogUtils.showDialog(ConfirmationDialogParams.LOGOUT, supportFragmentManager)
                    .buttonClicked.getSuccessStream().subscribe({ logout() })*/
    }

}

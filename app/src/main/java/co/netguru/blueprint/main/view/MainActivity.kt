package co.netguru.blueprint.main.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import co.netguru.blueprint.AppNavigation
import co.netguru.blueprint.BR
import co.netguru.blueprint.R
import co.netguru.blueprint.databinding.ActivityMainBinding
import co.netguru.blueprint.main.dependency.MainViewModelSubComponent
import co.netguru.blueprint.main.navigation.MainNavigationHelper
import co.netguru.blueprint.main.viewmodel.MainViewModel
import co.netguru.blueprintlibrary.common.dialogs.ConfirmationDialogParams
import co.netguru.blueprintlibrary.common.utils.HttpStatus
import co.netguru.blueprintlibrary.common.view.BaseActivity
import io.reactivex.disposables.Disposable
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
        compositeDisposable.add(handleAccountCreatedEventSuccess())
        compositeDisposable.add(handleLogoutEvent())
    }

    private fun handleAccountCreatedEventSuccess():Disposable{
        return accountCreatedEvent.getSuccessStream().subscribe({
            mainNavigationHelper.navigateNextScreenForActiveUser()
        })
    }

    private fun handleLogoutEvent() :Disposable {
        return repository.logoutEvent.getErrorStream().subscribe {
            handleError(it,this.javaClass)
            mainNavigationHelper.removeAllFragments()
            //mainNavigationHelper.getChatFragment()?.logout()
        }
    }

    fun logout() {
        val responseBody: ResponseBody = ResponseBody.create(MediaType.parse("text/plain"), "logout")
        val response: Response<String> = Response.error(HttpStatus.UNAUTHORIZED.value(), responseBody)
        repository.logoutEvent.onError(HttpException(response))
    }

    private fun showLogoutConfirmationDialog() {
            compositeDisposable.add(dialogUtils.showDialog(ConfirmationDialogParams.LOGOUT, supportFragmentManager)
                    .buttonClicked.getSuccessStream().subscribe { logout() })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.action_logout -> {
                showLogoutConfirmationDialog()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}

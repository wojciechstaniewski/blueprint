package co.netguru.blueprintlibrary.dependency.modules

import android.accounts.AccountManager
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import co.netguru.blueprintlibrary.common.Repository
import co.netguru.blueprintlibrary.common.utils.*
import co.netguru.blueprintlibrary.dependency.AccountType
import co.netguru.blueprintlibrary.dependency.AuthTokenType
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun provideSharedPrefsUtils(sharedPreferences: SharedPreferences): SharedPrefsUtils =
            SharedPrefsUtils(sharedPreferences)


    @Provides
    @Singleton
    fun provideFilesUtils(context: Context): FilesUtils = FilesUtils(context)

    @Provides
    @Singleton
    fun provideNotificationsUtils(context: Context): NotificationsUtils = NotificationsUtils(context)

    @Provides
    @Singleton
    fun provideAccountManager(context: Context): AccountManager = AccountManager.get(context)

    @Provides
    @Singleton
    fun provideAccountUtils(accountManager: AccountManager,
                            @AccountType accountType: String,
                            @AuthTokenType authTokenType: String) = AccountUtils(accountManager, accountType, authTokenType)

    @Provides
    @Singleton
    fun provideErrorUtils(accountUtils: AccountUtils, dialogUtils: DialogUtils,
                          @AccountType accountType: String) = ErrorUtils(accountUtils, dialogUtils, accountType)

    @Provides
    @Singleton
    fun provideDialogUtils() = DialogUtils()

    @Provides
    @Singleton
    fun provideRepository(): Repository = Repository()


}
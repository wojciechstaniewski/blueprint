package co.netguru.blueprintlibrary.dependency.modules

import android.accounts.AccountManager
import android.content.Context
import co.netguru.blueprintlibrary.dependency.AuthTokenType
import co.netguru.blueprintlibrary.dependency.AccountType
import co.netguru.blueprintlibrary.dependency.BaseUrl
import co.netguru.blueprintlibrary.common.interceptors.ConnectivityInterceptor
import co.netguru.blueprintlibrary.common.utils.AccountUtils
import co.netguru.blueprintlibrary.dependency.BaseSessionInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideApiHeaders(context: Context, @AccountType accountType: String,
                          @AuthTokenType authTokenType: String):
            ApiHeaders = ApiHeaders(context, accountType, authTokenType)

    @Provides
    @Singleton
    fun provideBaseSessionInterceptor(accountManager: AccountManager, accountUtils: AccountUtils,
                                      @AuthTokenType authTokenType: String)
            : BaseSessionInterceptor = BaseSessionInterceptor(accountManager, accountUtils, authTokenType)


    @Provides
    @Singleton
    fun provideOkHttpClient(context: Context, apiHeaders: ApiHeaders,
                            baseSessionInterceptor: BaseSessionInterceptor): OkHttpClient =
            OkHttpClient().newBuilder().addInterceptor(HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(apiHeaders)
                    .addInterceptor(baseSessionInterceptor)
                    .addInterceptor(ConnectivityInterceptor(context)).build()

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls().create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson, @BaseUrl url: String): Retrofit {
        return Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

}

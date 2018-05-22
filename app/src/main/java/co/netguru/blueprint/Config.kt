package co.netguru.blueprint

import android.content.Context

object Config {

    lateinit var SERVER: String

    fun init(context: Context) {
        SERVER = BuildConfig.SERVER
    }
}

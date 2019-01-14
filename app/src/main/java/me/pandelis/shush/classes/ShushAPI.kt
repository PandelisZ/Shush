package me.pandelis.shush.classes

import me.pandelis.shush.services.ShushService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class ShushAPI {

    companion object {
        private var INSTANCE: ShushService? = null

        fun getInstance(): ShushService? {
            if (INSTANCE == null) {
                synchronized(Retrofit::class) {
                    INSTANCE = Retrofit.Builder()
                        .baseUrl("https://shush-server.herokuapp.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(ShushService::class.java)
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
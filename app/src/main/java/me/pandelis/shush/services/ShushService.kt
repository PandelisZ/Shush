package me.pandelis.shush.services

import io.reactivex.Observable
import me.pandelis.shush.models.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ShushService {

    @GET("messages")
    fun getMessages(@Body user: GetMessage): Call<List<MessageResponse>>

    @POST("messages/send")
    fun send(@Body message: SendMessage): Call<Response>

    @POST("user/register")
    fun register(@Body profile: UpdateProfile): Call<Response>


    companion object {
        fun create(): ShushService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl("https://shush-server.herokuapp.com/")
                .build()

            return retrofit.create(ShushService::class.java)
        }
    }

}
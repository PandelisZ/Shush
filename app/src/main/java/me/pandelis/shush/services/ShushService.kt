package me.pandelis.shush.services

import me.pandelis.shush.models.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ShushService {

    @POST("messages")
    fun messages(@Body user: GetMessage): Call<List<MessageResponse>>

    @POST("messages/send")
    fun send(@Body message: SendMessage): Call<Response>

    @POST("user/register")
    fun register(@Body profile: UpdateProfile): Call<Response>

    @POST("user")
    fun user(@Body profile: UpdateProfile): Call<UserProfileResponse>

    @DELETE("messages/{id}/destroy")
    fun deleteMessage(@Path("id") messageId: Int): Call<Response>


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
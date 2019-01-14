package me.pandelis.shush.services

import me.pandelis.shush.models.GetMessage
import me.pandelis.shush.models.Response
import me.pandelis.shush.models.SendMessage
import me.pandelis.shush.models.UpdateProfile
import okhttp3.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ShushService {

    @GET("messages")
    fun getMessages(@Body user: GetMessage): Call

    @POST("messages/send")
    fun send(@Body message: SendMessage): Call

    @POST("user/register")
    fun register(@Body profile: UpdateProfile): Call

}
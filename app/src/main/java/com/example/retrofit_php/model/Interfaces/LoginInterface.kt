package com.example.retrofit_php.model.Interfaces

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginInterface {

    @FormUrlEncoded
    @POST("retro_login.php")
    fun getUserLogin(

        @Field("email") email : String,
        @Field("password") password : String,


    ): Call<String>
}
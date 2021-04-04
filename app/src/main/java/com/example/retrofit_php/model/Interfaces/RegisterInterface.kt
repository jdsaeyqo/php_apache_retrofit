package com.example.retrofit_php.model.Interfaces

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegisterInterface {

    @FormUrlEncoded
   @POST("retro_register.php")
   fun getUserRegist(
       @Field("name") name : String,
       @Field("email") email : String,
       @Field("password") password : String,

   ):Call<String>





}
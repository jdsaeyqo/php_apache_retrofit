package com.example.retrofit_php.model.Interfaces

import retrofit2.Call
import retrofit2.http.*

interface GetUserInfoInterface {

    @GET("retro_getuserinfo.php")
    fun getUserData(
        @Query("email") email : String


    ): Call<String>


    @GET("retro_getmatchuser.php")
    fun getMatchingUser(
        @Query("interest1") interest1 : String,
        @Query("interest2") interest2 : String,
        @Query("interest3") interest3 : String


    ): Call<String>
}
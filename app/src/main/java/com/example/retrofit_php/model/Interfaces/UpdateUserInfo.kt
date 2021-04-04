package com.example.retrofit_php.model.Interfaces

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UpdateUserInfo {

    @FormUrlEncoded
    @POST("retro_user_update.php")
    fun updateUserInfo(
        @Field("email") email : String,
        @Field("nickname") nickname : String?,
        @Field("age") age : String?,
        @Field("job") job : String?,
        @Field("interest1") interest1 : String?,
        @Field("interest2") interest2 : String?,
        @Field("interest3") interest3 : String?


    ): Call<String>
}
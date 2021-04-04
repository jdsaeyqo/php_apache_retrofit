package com.example.retrofit_php.model.data


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData(
    var email: String?,
    var nickname : String? = null,
    var age : String? = null,
    var job : String? = null,
    var interest1: String? = null,
    var interest2: String? = null,
    var interest3: String?= null

) : Parcelable {


}







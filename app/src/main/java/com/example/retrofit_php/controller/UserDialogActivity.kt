package com.example.retrofit_php.controller

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Display
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.retrofit_php.R
import com.example.retrofit_php.model.Interfaces.GetUserInfoInterface
import com.example.retrofit_php.model.Interfaces.Repository
import com.example.retrofit_php.model.data.UserData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_userdialog.*
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.fragment_user.view.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDialogActivity : AppCompatActivity(){

    lateinit var useremail : String
    lateinit var getuserinfoapi : GetUserInfoInterface
    lateinit var userData : UserData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userdialog)
//
//        val dm = application.resources.displayMetrics
//        val height = dm.heightPixels * 0.5
//        val width = dm.widthPixels * 0.7
//        window.attributes.width = width.toInt()
//        window.attributes.height = height.toInt()



        useremail = intent.getStringExtra("email").toString()


        getProfileImage()
        getUserInfo()

        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun getProfileImage() {
        useremail.let {
            FirebaseFirestore.getInstance().collection("profileImages").document(
                it
            ).addSnapshotListener { value, error ->
                if (value == null) return@addSnapshotListener

                if (value.data != null) {
                    val url = value.data!!["image"]

                    Glide.with(this).load(url).apply(RequestOptions().circleCrop())
                            .into(userProfileImageView)


                } else return@addSnapshotListener
            }
        }
    }

    private fun getUserInfo() {
        val retrofit = Repository.getApiClient()

        if (retrofit != null) {
            getuserinfoapi = retrofit.create(GetUserInfoInterface::class.java)
        }

        val call: Call<String> = getuserinfoapi.getUserData(useremail)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.e("onSuccessDialog", response.body()!!)

                    val jsonResponse = response.body()
                    try {
                        if (jsonResponse != null) {
                            val data = jsonResponse.substring(5)
                            val jsonObject = JSONObject(data)

                            val dataArray = jsonObject.getJSONArray("data")
                            for (i in 0 until dataArray.length()) {
                                val dataobj = dataArray.getJSONObject(i)

                                val nickname = dataobj.getString("nickname")
                                val age = dataobj.getString("age")
                                val job = dataobj.getString("job")
                                val interest1 = dataobj.getString("interest1")
                                val interest2 = dataobj.getString("interest2")
                                val interest3 = dataobj.getString("interest3")

                                userData = UserData(
                                    useremail, nickname, age, job, interest1, interest2, interest3
                                )

                                bindView(userData)

                            }

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })

    }

    private fun bindView(userData: UserData) {

        userNciknameTextView.text = userData.nickname
        if(userNciknameTextView.text == "null")  userNciknameTextView.text = "정보 없음"

        userJobTextView.text = userData.job
        if(userJobTextView.text == "null")  userJobTextView.text = "정보 없음"

        userInterest1TextView.text = userData.interest1
        if(userInterest1TextView.text == "null")  userInterest1TextView.text = "정보 없음"

        userInterest2TextView.text = userData.interest2
        if(userInterest2TextView.text == "null")  userInterest2TextView.text = "정보 없음"

        userInterest3TextView.text = userData.interest3
        if(userInterest3TextView.text == "null")  userInterest3TextView.text = "정보 없음"


    }


}
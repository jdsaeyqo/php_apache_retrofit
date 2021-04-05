package com.example.retrofit_php.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.retrofit_php.R
import com.example.retrofit_php.model.Interfaces.LoginInterface
import com.example.retrofit_php.model.Interfaces.Repository
import com.example.retrofit_php.model.data.UserData
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var  userData : UserData
    lateinit var loginapi : LoginInterface

    lateinit var name : String
    lateinit var email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        btnToRegister.setOnClickListener {

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            loginUser()

        }

    }

    private fun loginUser() {

        val email  = editLoginEmail.text.toString()
        val password  = editLoginPass.text.toString()

        val retrofit = Repository.getApiClient()

        if (retrofit != null) {
            loginapi = retrofit.create(LoginInterface::class.java)
        }

        val call : Call<String> = loginapi.getUserLogin(email,password)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.e("onSuccess", response.body()!!)

                    val jsonResponse = response.body()
                    try {
                        if (jsonResponse != null) {
                            parseLoginData(jsonResponse.substring(5))
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.message?.let { Log.e("onFailure", it) }
            }

        })

    }

    private fun parseLoginData(jsonResponse: String) {

        try {
            val jsonObject = JSONObject(jsonResponse)

            if(jsonObject.getString("status").equals("true")){
                saveInfo(jsonResponse)
                Toast.makeText(this,"로그인 성공", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show()

            }
        }catch (e : JSONException){
            e.printStackTrace()
        }

    }

    private fun saveInfo(jsonResponse: String) {

        try {
            val jsonObject = JSONObject(jsonResponse)
            if(jsonObject.getString("status").equals("true")){
                val dataArray = jsonObject.getJSONArray("data")

                for (i in 0 until dataArray.length()){

                    val dataobj = dataArray.getJSONObject(i)
                    email = dataobj.getString("email")

                }

                userData = UserData(email)

            }
        }catch (e : JSONException){
            e.printStackTrace()
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("userdata",userData)

        startActivity(intent)

    }
}
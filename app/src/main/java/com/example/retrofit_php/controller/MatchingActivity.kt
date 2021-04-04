package com.example.retrofit_php.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofit_php.R
import com.example.retrofit_php.model.Interfaces.GetUserInfoInterface
import com.example.retrofit_php.model.Interfaces.Repository
import com.example.retrofit_php.model.data.UserAdapter
import kotlinx.android.synthetic.main.activity_matching.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchingActivity : AppCompatActivity() {

    lateinit var userDATA: String
    private var email: String? = null
    private var userEmailList: MutableList<String> = mutableListOf()

    private val uAdapter = UserAdapter(this, userEmailList)
    lateinit var getmatchapi: GetUserInfoInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching)

        userRecyclerView.adapter = uAdapter
        val lm = LinearLayoutManager(this)
        userRecyclerView.layoutManager = lm
        userRecyclerView.setHasFixedSize(true)

        getInfo()

    }

    private fun getInfo() {
        email = intent.getStringExtra("useremail")

        val retrofit = Repository.getApiClient()
        if (retrofit != null) {
            getmatchapi = retrofit.create(GetUserInfoInterface::class.java)
        }
        val call: Call<String>? = email?.let { getmatchapi.getUserData(it) }

        call?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {

                if (response.isSuccessful && response.body() != null) {

                    userDATA = response.body()!!.substring(5)

                    val jsonResponse = JSONObject(userDATA)

                    val dataArray = jsonResponse.getJSONArray("data")

                    for (i in 0 until dataArray.length()) {

                        val dataobj = dataArray.getJSONObject(i)
                        val interest1 = dataobj.getString("interest1")
                        val interest2 = dataobj.getString("interest2")
                        val interest3 = dataobj.getString("interest3")

                        getMatchingUser(interest1,interest2,interest3)

                    }

                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {

                t.message?.let { Log.e("onFailure", it) }

            }

        })

    }

    private fun getMatchingUser(interest1: String, interest2: String, interest3: String) {

        val retrofit = Repository.getApiClient()
        if (retrofit != null) {
            getmatchapi = retrofit.create(GetUserInfoInterface::class.java)
        }
        val call: Call<String>? =getmatchapi.getMatchingUser(interest1,interest2,interest3)

        call?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {

                if (response.isSuccessful && response.body() != null) {

                    Log.d("matchAct",response.body().toString())
                    userDATA = response.body()!!.substring(5)


                    val jsonResponse = JSONObject(userDATA)

                    val dataArray = jsonResponse.getJSONArray("data")

                    for (i in 0 until dataArray.length()) {

                        val dataobj = dataArray.getJSONObject(i)
                        val useremail = dataobj.getString("email")

                        if(useremail != email){
                            userEmailList.add(useremail)
                        }

                    }
                    uAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {

                t.message?.let { Log.e("onFailure", it) }

            }


        })


    }

}
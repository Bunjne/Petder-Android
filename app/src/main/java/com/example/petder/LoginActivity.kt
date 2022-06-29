package com.example.petder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petder.Model.*
import com.example.petder.Remote.Get
import com.example.petder.URL.URL
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*


var filterInfo = listOf(Pair(-1, "All"), Pair(-1, 0), Pair(-1, 0), Pair(-1, "All"))
var userID = -1

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FuelManager.instance.basePath = "https://petder.azurewebsites.net/api/v1"

        btSignIn.setOnClickListener {
            var username = etUsername.text.toString()
            var password = etPassword.text.toString()

            when {
                username == "" && password == "" -> Toast.makeText(
                    this,
                    "Please put your username and password",
                    Toast.LENGTH_LONG
                ).show()

                username == "" -> Toast.makeText(
                    this,
                    "Please put your username",
                    Toast.LENGTH_LONG
                ).show()

                password == "" -> Toast.makeText(
                    this,
                    "Please put your password",
                    Toast.LENGTH_LONG
                ).show()

                else -> {
//                    userID = 10002
//                    var intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
                    getUserInfo(username, password)
                }
            }
        }

        btRegister.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUserInfo(username: String, password: String) {
        var success = true
        if (username == "" || password == "") {
            Toast.makeText(this, "Invalid Information", Toast.LENGTH_LONG).show()
        } else {
            val loginObj = LoginInfo(username, password)

            val httpAsync = URL.login.httpPost()
                .header("Content-Type" to "application/json")
                .body(Gson().toJson(loginObj))
                .responseString { _, _, result ->
                    result.fold({ data ->
                        // Convert json to obj to get response data
                        var dataResponse = Gson().fromJson(data, LoginResponse::class.java)
                        var userId = dataResponse.userId

                        Fuel.get(URL.getCurrentPet(userId))
                            .responseString { request, response, result ->
                                result.fold({ data ->
                                    var intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                },
                                    { error ->
                                        var intent = Intent(this, CreateActivity::class.java)
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        startActivity(intent)
                                    })
                            }
                    }
                        , { error ->
                            success = false
                            Log.e("error", error.message)
                        })
                }
            if (success)
                userID =
                    Gson().fromJson(httpAsync.join().data.toString(Charsets.UTF_8), LoginResponse::class.java).userId
        }
        println(userID)
    }
}

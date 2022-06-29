package com.example.petder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.petder.Model.RegisterInfo
import com.example.petder.Remote.Post
import com.example.petder.URL.URL
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btDone_register.setOnClickListener {
            var username = etUsername_register.text.toString()
            var password = etPassword_register.text.toString()
            var location = etAddress_register.text.toString()
            var phoneNumber = etPhone_register.text.toString()

            registerUser(username, password, location, phoneNumber)
        }

        ivBack_register.setOnClickListener {
            finishAndRemoveTask()
        }
    }

    private fun registerUser(username: String, password: String, location: String, phoneNumber: String) {
        if (username == "" || password == "" || location == "" || phoneNumber == "") {
            Toast.makeText(this, "Invalid Information", Toast.LENGTH_LONG).show()
        } else {
            var registerObj = RegisterInfo(username, password, location, phoneNumber)
            var registerResponse = Post().register(registerObj)
            if(registerResponse == 200) {
                Toast.makeText(this, "Successfully registration", Toast.LENGTH_LONG).show()
                finishAndRemoveTask()
            } else {
                println(registerResponse)
                Toast.makeText(this, "This username has been used", Toast.LENGTH_LONG).show()
            }
        }
    }
}

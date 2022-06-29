package com.example.petder.Model

import java.text.SimpleDateFormat
import java.util.*

class Global {

    private var currentTime = ""

    fun getHHmm(): String {
        currentTime = SimpleDateFormat("H:mm").format(Date())
        return currentTime
    }
}
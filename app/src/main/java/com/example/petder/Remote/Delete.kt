package com.example.petder.Remote

import com.example.petder.URL.URL
import com.github.kittinunf.fuel.httpDelete
import java.net.IDN

class Delete {

    fun deletePetById(petID: Int) : Int {
        var http = URL.deletePet(petID).httpDelete()
            .responseString { _, _, result ->
                result.fold({}, {})
            }
        return http.join().statusCode
    }

}
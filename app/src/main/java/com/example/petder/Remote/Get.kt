package com.example.petder.Remote

import android.util.Log
import com.example.petder.Model.*
import com.example.petder.URL.URL
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception

class Get {

    fun getCurrentPet(userId: Int): CurrentPetResponse {
        var httpAsync = URL.getCurrentPet(userId).httpGet()
            .responseString { _, _, result ->
                result.fold({ data ->

                },
                    { error ->
                        Log.e("error", "failed get current pet")
                    })
            }
        return Gson().fromJson(httpAsync.join().data.toString(Charsets.UTF_8), CurrentPetResponse::class.java)
    }

    fun getPetInfoEdit(userID: Int): PetInfoEditResponse {
        var http = URL.getPetInfoEdit(userID).httpGet()
            .responseString { _, _, result -> result.fold({ }, { }) }
        return Gson().fromJson(http.join().data.toString(Charsets.UTF_8), PetInfoEditResponse::class.java)
    }

    fun getAllBreed(): List<AllBreedResponse> {
        var http = URL.breed.httpGet()
            .responseString { _, _, result -> result.fold({}, {}) }
        return Gson().fromJson(http.join().data.toString(Charsets.UTF_8), Array<AllBreedResponse>::class.java).toList()
    }

    fun getAllPetExceptCurrent(userID: Int): List<AllPetResponse> {
        var http = URL.getAllPetExceptCurrent(userID).httpGet()
            .responseString { _, _, result -> result.fold({}, {}) }
        return try {
            Gson().fromJson(http.join().data.toString(Charsets.UTF_8), Array<AllPetResponse>::class.java).toList()
        } catch (exception: Exception) {
            emptyList()
        }
    }

    fun getRequest(userID: Int): List<RequestResponse> {
        var http = URL.getRequest(userID).httpGet()
            .responseString { _, _, result -> result.fold({}, {}) }
        return try {
            Gson().fromJson(http.join().data.toString(Charsets.UTF_8), Array<RequestResponse>::class.java).toList()
        } catch (exception: Exception) {
            emptyList()
        }
    }

    fun getAllFilters(): AllFiltersResponse {
        var http = URL.allFilters.httpGet()
            .responseString { _, _, result ->
                result.fold({}, {})
            }
        return Gson().fromJson(http.join().data.toString(Charsets.UTF_8), AllFiltersResponse::class.java)
    }

    fun getRank(): List<RankInfoResponse> {
        var http = URL.rank.httpGet(listOf("pageSize" to 10))
            .responseString { _, _, result -> result.fold({}, {}) }
        return try {
            Gson().fromJson(http.join().data.toString(Charsets.UTF_8), Array<RankInfoResponse>::class.java).toList()
        } catch (exception: Exception) {
            emptyList()
        }
    }

    fun getOtherPetInfo(userID: Int, petID: Int, filter: List<Pair<Int, Any>>): List<OtherPetInfoResponse> {
        var filterArr = arrayListOf("gender", "breedId", "age", "address")
        var url = URL.getOtherPetInfo(userID, petID)
        var queryInfo = ""
        for (i in filter.indices) {
            if (filter[i].second != "All" && filter[i].second != 0) {
                if(i == 1) {
                    queryInfo += "${filterArr[i]}=${filter[i].first}&"
                } else {
                    queryInfo += "${filterArr[i]}=${filter[i].second}&"
                }

            }
        }

        if (queryInfo != "") url += "${queryInfo.substring(0, queryInfo.length - 1)}"
        println(url)
        var http = url.httpGet()
            .responseString { _, _, result -> result.fold({}, {}) }
        return try {
            Gson().fromJson(http.join().data.toString(Charsets.UTF_8), Array<OtherPetInfoResponse>::class.java).toList()
        } catch (exception: Exception) {
            emptyList()
        }
    }

    fun getAllSessions(petID: Int): List<Session> {
        var http = URL.getAllSessions(petID).httpGet()
            .responseString { _, _, result -> result.fold({}, {}) }
        return try {
            Gson().fromJson(http.join().data.toString(Charsets.UTF_8), Array<Session>::class.java).toList()
        } catch (exception: Exception) {
            emptyList()
        }
    }

    fun getAllMessages(sessionId: Int, senderId: Int, receiverPetId: Int): List<AllMessages> {
        var http = URL.getAllMessages(sessionId, senderId, receiverPetId).httpGet(listOf("pageSize" to 20))
            .responseString { _, _, result -> result.fold({}, {}) }
        return try {
            Gson().fromJson(http.join().data.toString(Charsets.UTF_8), Array<AllMessages>::class.java).toList()
        } catch (exception: Exception) {
            emptyList()
        }
    }
}
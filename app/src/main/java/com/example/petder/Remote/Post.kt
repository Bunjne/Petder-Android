package com.example.petder.Remote


import android.util.Log
import com.example.petder.Model.*
import com.example.petder.URL.URL
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import java.lang.Exception
import java.util.*

class Post {

    fun register(registerInfo: RegisterInfo): Int {

        var http = URL.register.httpPost()
            .header("Content-Type" to "application/json")
            .body(Gson().toJson(registerInfo))
            .responseString { _, _, result ->
                result.fold({ data ->
                }
                    , { error ->
                    })
            }
        println(http.join())
        return http.join().statusCode
    }

    fun createNewCat(userID: Int, catInfoJson: String): Int {
        var http = URL.createNewPet(userID).httpPost()
            .header("Content-Type" to "application/json")
            .body(catInfoJson)
            .responseString { _, _, result ->
                result.fold({ data ->
                }
                    , { error ->
                        Log.e("error", error.message)
                    })
            }
        return http.join().statusCode
    }

    fun changeProfileImage(imageID: Int, url: String): Int {
        var status = false
        var http = URL.changeProfileImage(imageID).httpPost()
            .header("Content-Type" to "application/json")
            .body(Gson().toJson(ProfileImage(url)))
            .responseString { _, _, result ->
                result.fold({ data ->
                    Log.e("success", "Successfully upload")
                }
                    , { error ->
                        Log.e("error", error.message)
                    })
            }
        return http.join().statusCode
    }

    fun editPetInfo(userID: Int, petID: Int, petInfoEdit: PetInfoEdit): Int {
        var http = URL.editPetInfo(userID, petID).httpPost()
            .header("Content-Type" to "application/json")
            .body(Gson().toJson(petInfoEdit))
            .responseString { _, _, result ->
                result.fold({ data ->
                    Log.e("success", "Successfully Edit")
                }
                    , { error ->
                        Log.e("error", error.message)
                    })
            }
        return http.join().statusCode
    }

    fun changeCurrentPet(oldPetId: Int, newPetId: Int): Int {
        var obj = CurrentPetChange(oldPetId, newPetId)
        var http = URL.changeCurrentPet.httpPost()
            .header("Content-Type" to "application/json")
            .body(Gson().toJson(obj))
            .responseString { _, _, result ->
                result.fold({ data ->
                    Log.e("success", "Successfully change")
                }
                    , { error ->
                        Log.e("error", error.message)
                    })
            }
        return http.join().statusCode
    }

    fun acceptRequest(requestID: Int): AcceptRequestResponse {
        var obj = AcceptRequest(requestID)
        var http = URL.acceptRequest.httpPost()
            .header("Content-Type" to "application/json")
            .body(Gson().toJson(obj))
            .responseString { _, _, result ->
                result.fold({ data ->
                    Log.e("success", "Successfully change")
                }
                    , { error ->
                        Log.e("error", error.message)
                    })
            }
        return Gson().fromJson(http.join().data.toString(Charsets.UTF_8), AcceptRequestResponse::class.java)
    }

    fun blockPet(blocker: Int, blocked: Int): Int {
        var http = URL.block.httpPost()
            .header("Content-Type" to "application/json")
            .body(Gson().toJson(BlockPet(blocker, blocked)))
            .responseString { _, _, result ->
                result.fold({}, {})
            }
        return http.join().statusCode
    }

    fun sendRequest(resquesterID: Int, requestedID: Int): Int {
        var http = URL.sendRequest.httpPost()
            .header("Content-Type" to "application/json")
            .body(Gson().toJson(LikesRequest(resquesterID, requestedID)))
            .responseString { _, _, result ->
                result.fold({}, {})
            }
        return http.join().statusCode
    }

    fun like(resquesterID: Int, requestedID: Int): Int {
        var http = URL.like.httpPost()
            .header("Content-Type" to "application/json")
            .body(Gson().toJson(LikesRequest(resquesterID, requestedID)))
            .responseString { _, _, result ->
                result.fold({}, {})
            }
        return http.join().statusCode
    }

    fun sendMessage(sessionId: Int, message: String, senderId: Int): SendMessageResponse? {
        var http = URL.sendMessage.httpPost()
            .header("Content-Type" to "application/json")
            .body(Gson().toJson(SendMessage(sessionId, message, senderId)))
            .responseString { _, _, result ->
                result.fold({}, {})
            }
        return try {
            Gson().fromJson(http.join().data.toString(Charsets.UTF_8), SendMessageResponse::class.java)
        } catch (exception: Exception) {
            null
        }
    }

    fun unsentMessage(messageId: Int): Int {
        var http = URL.unsentMessage.httpPost()
            .header("Content-Type" to "application/json")
            .body(Gson().toJson(UnsentMessage(messageId)))
            .responseString { _, _, result ->
                result.fold({}, {})
            }
        return http.join().statusCode
    }
}
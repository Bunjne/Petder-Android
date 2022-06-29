package com.example.petder.Model

import android.net.Uri


data class PetInfo1(
    var mainProfile: String?,
    var urlProfile: ArrayList<String>?,
    var name: String,
    var breeding: String,
    var age: String,
    var gender: String,
    var location: String
)

data class RankInfo(var petInfo1: PetInfo1, var numLike: Int)

data class Message(var message: String, var time: String, var isSender: Boolean)

// new
data class RegisterInfo(var username: String, var password: String, var address: String, var phoneNumber: String)

data class LoginInfo(var username: String, var password: String)

data class PetInfo(
    var Name: String,
    var BreedId: Int,
    var BirthDateTime: String,
    var Description: String,
    var Gender: String,
    var PetImages: ArrayList<ImageURL>
)

data class ImageURL(var ImageUrls: String, var IsProfile: Boolean)

data class ProfileImage(var ImageUrl: String)

data class PetInfoEdit(
    var breedId: Int,
    var name: String,
    var gender: String,
    var birthDateTime: String,
    var description: String,
    var isCurrent: Boolean,
    var address: String,
    var petImages: ArrayList<ImageURLEdit>
)

data class ImageURLEdit(var imageId: Int, var imageUrl: String, var isProfile: Boolean)

data class CurrentPetChange(var OldPetId: Int, var NewPetId: Int)

data class AcceptRequest(var RequestListId: Int)

data class BlockPet(var blockerPetId: Int, var blockedPetId: Int)

data class LikesRequest(var RequesterPetId: Int, var RequestedPetId: Int)

data class SendMessage(var SessionId: Int, var Message: String, var SenderId: Int)

data class UnsentMessage(var MessageId: Int)
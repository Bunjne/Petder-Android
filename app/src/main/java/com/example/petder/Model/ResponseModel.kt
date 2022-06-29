package com.example.petder.Model

data class LoginResponse(var userId: Int)

data class CurrentPetResponse(
    var petId: Int,
    var imageId: Int,
    var name: String,
    var breed: String,
    var address: String,
    var imageUrl: String
)

data class PetInfoEditResponse(
    var userId: Int,
    var petId: Int,
    var breedId: Int,
    var name: String,
    var gender: String,
    var birthDateTime: String,
    var description: String,
    var isCurrent: Boolean,
    var address: String,
    var petImages: ArrayList<ImageURLEdit>

)

data class AllBreedResponse(var breedId: Int, var name: String)

data class AllPetResponse(var imageUrl: String, var name: String, var breed: String, var petId: Int)

data class RequestResponse(
    var requestListId: Int,
    var imageUrl: String,
    var breed: String,
    var requesterPetId: Int,
    var name: String
)

data class AcceptRequestResponse(var SessionId: Int, var CreateDateTIme: String)

data class AllFiltersResponse(
    var ages: ArrayList<Int>,
    var breedings: ArrayList<Breed>,
    var genders: ArrayList<String>,
    var addresses: ArrayList<String>
)

data class Breed(var breed: String, var breedingId: Int)

data class RankInfoResponse(var name: String, var imageUrl: String, var numberOfLikes: Int)

data class OtherPetInfoResponse(
    var petId: Int,
    var name: String,
    var breed: String,
    var address: String,
    var imageUrls: ArrayList<String>
)

data class Session(
    var sessionId: Int,
    var receiverPetId: Int,
    var name: String,
    var imageUrl: String,
    var message: String,
    var sentDateTime: String
)

data class AllMessages(
    var senderName: String,
    var senderImageUrl: String,
    var senderId: Int,
    var sendDateTime: String,
    var message: String,
    var isUnsent: Boolean,
    var isSystemMessage: Boolean,
    var messageId: Int
)

data class SendMessageResponse(
    var message_id: Int,
    var session_id: Int,
    var sender_pet_id: Int,
    var message: String,
    var sent_datetime: String,
    var is_unsent: Boolean,
    var is_system_message: Boolean
)
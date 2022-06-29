package com.example.petder.URL


class URL {

    companion object {
        const val login = "/authentication/login"
        const val register = "/authentication/register"
        const val breed = "/profile/breeds"
        const val changeCurrentPet = "/profile/pets/edit-pets"
        const val acceptRequest = "/request/requests"
        const val allFilters = "/request/filters"
        const val rank = "/request/ranks"
        const val block = "/request/blocks"
        const val like = "/request/likes"
        const val sendRequest = "/request/matches"
        const val sendMessage = "/chat/messages"
        const val unsentMessage = "/chat/unsent"

        fun createNewPet(userID: Int) = "/profile/users/$userID/pets"

        fun changeProfileImage(userID: Int) = "/profile/images/$userID"

        fun getCurrentPet(userID: Int) = "/profile/users/$userID/current-pet-profiles"

        fun getPetInfoEdit(userID: Int) = "/profile/users/$userID/current-pets"

        fun editPetInfo(userID: Int, petID: Int) = "/profile/users/$userID/edit-pets/$petID"

        fun getAllPetExceptCurrent(userID: Int) = "/profile/users/$userID/pets"

        fun getRequest(userID: Int) = "/request/pending-requests/$userID"

        fun deletePet(petID: Int) = "/request/pets/$petID"

        fun getOtherPetInfo(userID: Int, petID: Int) = "/request/pets/$userID/$petID?pageSize=10&"

        fun getAllSessions(petID: Int) = "/chat/pets/$petID/sessions"

        fun getAllMessages(sessionId: Int, senderId: Int, receiverpetId: Int) =
            "/chat/sessions/$sessionId/$senderId/$receiverpetId"
    }
}
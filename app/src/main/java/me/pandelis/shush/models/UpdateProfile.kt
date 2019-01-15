package me.pandelis.shush.models

class UpdateProfile(
    val name: String?,
    val publicKey: String,
    val firebaseId: String?
)

class UserProfileResponse(
    val id: String,
    val name: String
)


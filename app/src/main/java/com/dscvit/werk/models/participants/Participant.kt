package com.dscvit.werk.models.participants


import com.google.gson.annotations.SerializedName

data class Participant(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("joined")
    val joined: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("points")
    val points: Int,
    @SerializedName("sessionId")
    val sessionId: Int,
    @SerializedName("userId")
    val userId: Int
)
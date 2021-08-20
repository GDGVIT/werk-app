package com.dscvit.werk.models.sessions


import com.google.gson.annotations.SerializedName

data class Participant(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String
)
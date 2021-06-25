package com.dscvit.werk.models.sessions


import com.google.gson.annotations.SerializedName

data class CreatedBy(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("userId")
    val userId: Int
)
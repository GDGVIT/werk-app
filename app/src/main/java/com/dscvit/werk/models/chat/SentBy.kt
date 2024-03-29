package com.dscvit.werk.models.chat


import com.google.gson.annotations.SerializedName

data class SentBy(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("userId")
    val userId: Int
)
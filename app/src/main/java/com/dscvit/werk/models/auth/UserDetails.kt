package com.dscvit.werk.models.auth


import com.google.gson.annotations.SerializedName

data class UserDetails(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("userId")
    val userId: Int
)
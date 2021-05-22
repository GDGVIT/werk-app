package com.dscvit.werk.models.auth


import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("password")
    val password: String
)
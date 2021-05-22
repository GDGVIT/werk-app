package com.dscvit.werk.models.auth


import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("userDetails")
    val userDetails: UserDetails
)
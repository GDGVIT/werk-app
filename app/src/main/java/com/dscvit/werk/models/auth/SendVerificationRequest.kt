package com.dscvit.werk.models.auth

import com.google.gson.annotations.SerializedName

data class SendVerificationRequest(
    @SerializedName("email")
    val email: String
)
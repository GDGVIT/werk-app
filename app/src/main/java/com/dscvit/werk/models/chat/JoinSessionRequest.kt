package com.dscvit.werk.models.chat


import com.google.gson.annotations.SerializedName

data class JoinSessionRequest(
    @SerializedName("session")
    val sessionID: Int
)
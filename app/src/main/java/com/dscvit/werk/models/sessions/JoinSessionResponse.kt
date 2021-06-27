package com.dscvit.werk.models.sessions


import com.google.gson.annotations.SerializedName

data class JoinSessionResponse(
    @SerializedName("session")
    val session: CreateSession
)
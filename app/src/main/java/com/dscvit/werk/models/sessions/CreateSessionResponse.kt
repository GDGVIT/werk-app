package com.dscvit.werk.models.sessions


import com.google.gson.annotations.SerializedName

data class CreateSessionResponse(
    @SerializedName("session")
    val session: CreateSession
)
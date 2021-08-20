package com.dscvit.werk.models.sessions


import com.google.gson.annotations.SerializedName

data class SessionsResponse(
    @SerializedName("sessions")
    val sessions: List<Session>
)
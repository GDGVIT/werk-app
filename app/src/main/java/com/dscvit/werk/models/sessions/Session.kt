package com.dscvit.werk.models.sessions


import com.google.gson.annotations.SerializedName

data class Session(
    @SerializedName("participants")
    val participants: List<Participant>,
    @SerializedName("participantsCount")
    val participantsCount: Int,
    @SerializedName("session")
    val sessionDetails: SessionDetails
)
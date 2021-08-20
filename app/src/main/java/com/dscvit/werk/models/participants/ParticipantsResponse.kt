package com.dscvit.werk.models.participants


import com.google.gson.annotations.SerializedName

data class ParticipantsResponse(
    @SerializedName("participants")
    val participants: List<Participant>,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("user")
    val user: Participant
)
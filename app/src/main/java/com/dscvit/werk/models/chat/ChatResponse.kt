package com.dscvit.werk.models.chat


import com.google.gson.annotations.SerializedName

data class ChatResponse(
    @SerializedName("oldMessages")
    val oldMessages: List<Message>
)
package com.dscvit.werk.models.chat

import com.google.gson.annotations.SerializedName

data class ChatMessage(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: String
)
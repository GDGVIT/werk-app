package com.dscvit.werk.models.chat


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("message")
    val message: String,
    @SerializedName("messageId")
    val messageId: Int,
    @SerializedName("sender")
    val sender: Boolean,
    @SerializedName("sentBy")
    val sentBy: SentBy,
    @SerializedName("sentTime")
    val sentTime: Long
)
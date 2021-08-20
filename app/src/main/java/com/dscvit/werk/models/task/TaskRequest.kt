package com.dscvit.werk.models.task


import com.google.gson.annotations.SerializedName

data class TaskRequest(
    @SerializedName("sessionId")
    val sessionId: Int
)
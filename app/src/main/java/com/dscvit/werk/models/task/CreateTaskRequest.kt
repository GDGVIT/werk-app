package com.dscvit.werk.models.task


import com.google.gson.annotations.SerializedName

data class CreateTaskRequest(
    @SerializedName("description")
    val description: String,
    @SerializedName("expectedDuration")
    val expectedDuration: Int,
    @SerializedName("points")
    val points: Int,
    @SerializedName("sessionId")
    var sessionId: Int,
    @SerializedName("title")
    val title: String
)
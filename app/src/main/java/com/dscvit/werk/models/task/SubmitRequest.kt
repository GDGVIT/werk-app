package com.dscvit.werk.models.task


import com.google.gson.annotations.SerializedName

data class SubmitRequest(
    @SerializedName("completedDuration")
    val completedDuration: Int
)
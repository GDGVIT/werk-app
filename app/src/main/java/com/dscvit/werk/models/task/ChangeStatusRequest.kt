package com.dscvit.werk.models.task


import com.google.gson.annotations.SerializedName

data class ChangeStatusRequest(
    @SerializedName("statusCode")
    val statusCode: Int
)
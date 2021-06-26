package com.dscvit.werk.models.sessions


import com.google.gson.annotations.SerializedName

data class CreateSessionRequest(
    @SerializedName("description")
    val description: String,
    @SerializedName("endTime")
    val endTime: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("participants")
    val participants: List<String>,
    @SerializedName("startTime")
    val startTime: Long,
    @SerializedName("taskAssignByAll")
    val taskAssignByAll: Int,
    @SerializedName("taskCreationByAll")
    val taskCreationByAll: Int
)
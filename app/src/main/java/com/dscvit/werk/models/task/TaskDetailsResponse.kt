package com.dscvit.werk.models.task


import com.google.gson.annotations.SerializedName

data class TaskDetailsResponse(
    @SerializedName("assignedTo")
    val assignedTo: Int,
    @SerializedName("completionDuration")
    val completionDuration: Long,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("createdDate")
    val createdDate: Long,
    @SerializedName("description")
    val description: String,
    @SerializedName("elapsedHours")
    val elapsedHours: Int,
    @SerializedName("elapsedMins")
    val elapsedMins: Int,
    @SerializedName("elapsedSecs")
    val elapsedSecs: Int,
    @SerializedName("expectedDuration")
    val expectedDuration: Int,
    @SerializedName("givenIn")
    val givenIn: Int,
    @SerializedName("points")
    val points: Int,
    @SerializedName("startedTime")
    val startedTime: Long,
    @SerializedName("status")
    val status: String,
    @SerializedName("submittedDate")
    val submittedDate: Any,
    @SerializedName("taskId")
    val taskId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)
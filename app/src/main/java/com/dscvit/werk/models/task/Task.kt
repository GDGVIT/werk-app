package com.dscvit.werk.models.task


import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("assigned")
    val assigned: Assigned,
    @SerializedName("assignedTo")
    val assignedTo: Int,
    @SerializedName("completionDuration")
    val completionDuration: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("createdDate")
    val createdDate: Long,
    @SerializedName("creator")
    val creator: Creator,
    @SerializedName("description")
    val description: String,
    @SerializedName("expectedDuration")
    val expectedDuration: Int,
    @SerializedName("givenIn")
    val givenIn: Int,
    @SerializedName("points")
    val points: Double,
    @SerializedName("status")
    val status: String,
    @SerializedName("submittedDate")
    val submittedDate: Long,
    @SerializedName("taskId")
    val taskId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)
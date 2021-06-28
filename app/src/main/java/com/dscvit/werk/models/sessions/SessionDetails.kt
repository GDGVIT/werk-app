package com.dscvit.werk.models.sessions


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SessionDetails(
    @SerializedName("accessCode")
    val accessCode: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: CreatedBy,
    @SerializedName("endTime")
    val endTime: Long,
    @SerializedName("sessionDescription")
    val sessionDescription: String,
    @SerializedName("sessionId")
    val sessionId: Int,
    @SerializedName("sessionName")
    val sessionName: String,
    @SerializedName("startTime")
    val startTime: Long,
    @SerializedName("taskAssignUniv")
    val taskAssignUniv: Boolean,
    @SerializedName("taskCreationUniv")
    val taskCreationUniv: Boolean,
    @SerializedName("updatedAt")
    val updatedAt: String
): Parcelable
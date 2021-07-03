package com.dscvit.werk.models.task


import com.google.gson.annotations.SerializedName

data class TaskResponse(
    @SerializedName("tasks")
    val tasks: List<Task>
)
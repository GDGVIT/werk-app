package com.dscvit.werk.models.participants


import com.google.gson.annotations.SerializedName

data class AssignRequest(
    @SerializedName("userId")
    val userId: Int
)
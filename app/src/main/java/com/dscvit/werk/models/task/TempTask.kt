package com.dscvit.werk.models.task

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TempTask(
    val id: Int,
    val title: String,
    val description: String,
    val isTaskStarted: Boolean
) : Parcelable
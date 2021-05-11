package com.dscvit.werk.ui.tasks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dscvit.werk.R
import com.dscvit.werk.databinding.ActivityTaskDescriptionBinding

class TaskDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDescriptionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}
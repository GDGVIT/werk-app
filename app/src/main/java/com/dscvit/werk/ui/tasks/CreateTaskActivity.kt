package com.dscvit.werk.ui.tasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dscvit.werk.R
import com.dscvit.werk.databinding.ActivityCreateTaskBinding

class CreateTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBarTitle.text = "Create Task"

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}
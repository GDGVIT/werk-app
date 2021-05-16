package com.dscvit.werk.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.dscvit.werk.R
import com.dscvit.werk.databinding.ActivityTaskDescriptionBinding

class TaskDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDescriptionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.appBarTitle.text = "Task Name"

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.addMembersCard.setOnClickListener {

        }
    }
}
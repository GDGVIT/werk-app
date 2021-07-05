package com.dscvit.werk.ui.tasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dscvit.werk.R
import com.dscvit.werk.databinding.ActivityCreateTaskBinding
import com.dscvit.werk.models.task.CreateTaskRequest
import com.dscvit.werk.ui.overview.CreateSessionFragmentDirections
import com.dscvit.werk.ui.overview.OverviewViewModel
import com.dscvit.werk.ui.utils.buildLoader
import com.dscvit.werk.ui.utils.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CreateTaskActivity : AppCompatActivity() {
    private val TAG: String = this.javaClass.simpleName

    private lateinit var binding: ActivityCreateTaskBinding

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBarTitle.text = "Create Task"

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.createButton.setOnClickListener {
            if (binding.taskTitleInput.editText!!.text.isNotEmpty() &&
                binding.taskDescriptionInput.editText!!.text.isNotEmpty() &&
                binding.taskDurationInput.editText!!.text.isNotEmpty() &&
                binding.taskPointsInput.editText!!.text.isNotEmpty()
            ) {
                val createTaskRequest = CreateTaskRequest(
                    binding.taskDescriptionInput.editText!!.text.toString(),
                    binding.taskDurationInput.editText!!.text.toString().toInt(),
                    binding.taskPointsInput.editText!!.text.toString().toInt(),
                    0,
                    binding.taskTitleInput.editText!!.text.toString()
                )

                viewModel.createATask(createTaskRequest)
            } else {
                it.showErrorSnackBar("All fields are mandatory")
            }
        }

        val loader = buildLoader()

        lifecycleScope.launchWhenResumed {
            viewModel.createTask.collect { event ->
                when (event) {
                    is TaskViewModel.CreateTaskEvent.Loading -> {
                        Log.d(TAG, "LOADING....")
                        loader.show()
                    }
                    is TaskViewModel.CreateTaskEvent.Failure -> {
                        Log.d(TAG, event.errorMessage)
                        loader.hide()
                        binding.root.showErrorSnackBar(event.errorMessage)
                    }
                    is TaskViewModel.CreateTaskEvent.Success -> {
                        loader.hide()
                        finish()
                        Log.d(TAG, "TASK CREATED: ${event.task}")
                    }
                    else -> {
                    }
                }
            }
        }
    }
}
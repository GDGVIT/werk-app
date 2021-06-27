package com.dscvit.werk.ui.overview

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dscvit.werk.R
import com.dscvit.werk.databinding.FragmentCreateSessionBinding
import com.dscvit.werk.models.sessions.CreateSessionRequest
import com.dscvit.werk.ui.utils.buildLoader
import com.dscvit.werk.ui.utils.showErrorSnackBar
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

class CreateSessionFragment : Fragment() {
    private val TAG: String = this.javaClass.simpleName

    private var _binding: FragmentCreateSessionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OverviewViewModel by activityViewModels()

    private var startDateTimeStr = ""
    private var endDateTimeStr = ""
    private val emailList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.appBarTitle.text = requireContext().getString(R.string.create_a_session)
        binding.appBar.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.startDateInput.editText!!.setOnClickListener {
            val dateBuilder = MaterialDatePicker.Builder.datePicker()
            val datePicker = dateBuilder.build()
            datePicker.show(childFragmentManager, dateBuilder.toString())

            datePicker.addOnPositiveButtonClickListener {
                Log.d(
                    "DatePickerActivity",
                    "Date String = ${datePicker.headerText}:: Date epoch value = $it"
                )
                binding.startDateInput.editText!!.setText(datePicker.headerText)
                startDateTimeStr += datePicker.headerText
            }
        }

        binding.startTimeInput.editText!!.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Session Time")
                .build()

            timePicker.show(childFragmentManager, timePicker.toString())
            timePicker.addOnPositiveButtonClickListener {
                binding.startTimeInput.editText!!.setText("${timePicker.hour} : ${timePicker.minute}")
                startDateTimeStr += " ${timePicker.hour}:${timePicker.minute}"
            }
        }

        binding.endDateInput.editText!!.setOnClickListener {
            val dateBuilder = MaterialDatePicker.Builder.datePicker()
            val datePicker = dateBuilder.build()
            datePicker.show(childFragmentManager, dateBuilder.toString())

            datePicker.addOnPositiveButtonClickListener {
                Log.d(
                    "DatePickerActivity",
                    "End Date String = ${datePicker.headerText}:: Date epoch value = $it"
                )
                binding.endDateInput.editText!!.setText(datePicker.headerText)
                endDateTimeStr += datePicker.headerText
            }
        }

        binding.endTimeInput.editText!!.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Session Time")
                .build()

            timePicker.show(childFragmentManager, timePicker.toString())
            timePicker.addOnPositiveButtonClickListener {
                binding.endTimeInput.editText!!.setText("${timePicker.hour} : ${timePicker.minute}")
                endDateTimeStr += " ${timePicker.hour}:${timePicker.minute}"
            }
        }

        binding.emailInput.editText!!.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val email = textView.text.toString()
                textView.text = null
                addEmailChipToGroup(email)
                return@setOnEditorActionListener true
            }
            false
        }

        binding.nextButton.setOnClickListener {
//            try {
//                if (binding.sessionNameInput.editText!!.text.toString()
//                        .isNotEmpty() && binding.startDateInput.editText!!.text.toString()
//                        .isNotEmpty() && binding.startTimeInput.editText!!.text.toString()
//                        .isNotEmpty() && binding.endDateInput.editText!!.text.toString()
//                        .isNotEmpty() && binding.endTimeInput.editText!!.text.toString()
//                        .isNotEmpty() && binding.descriptionInput.editText!!.text.toString()
//                        .isNotEmpty() && emailList.isNotEmpty()
//                ) {
//                    val formatter = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH)
//                    val startDateTime = formatter.parse(startDateTimeStr)
//                    val endDateTime = formatter.parse(endDateTimeStr)
//
//                    val createSessionRequest =
//                        CreateSessionRequest(
//                            binding.descriptionInput.editText!!.text.toString(),
//                            endDateTime!!.time,
//                            binding.sessionNameInput.editText!!.text.toString(),
//                            emailList,
//                            startDateTime!!.time,
//                            binding.memberAssignTaskCheck.isChecked,
//                            binding.memberCreateTaskCheck.isChecked,
//                        )
//
//                    Log.d(TAG, createSessionRequest.toString())
//
//                    viewModel.createASession(createSessionRequest)
//                } else {
//                    view.showErrorSnackBar("All fields are required.")
//                }
//            } catch (e: Exception) {
//                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
//            }

            val action =
                CreateSessionFragmentDirections.actionCreateSessionFragmentToInviteFragment()
            findNavController().navigate(action)
        }

        val loader = requireContext().buildLoader()

        lifecycleScope.launchWhenCreated {
            viewModel.createSession.collect { event ->
                when (event) {
                    is OverviewViewModel.CreateSessionEvent.Loading -> {
                        Log.d(TAG, "LOADING....")
                        loader.show()
                    }
                    is OverviewViewModel.CreateSessionEvent.Failure -> {
                        Log.d(TAG, event.errorMessage)
                        loader.hide()
                        view.showErrorSnackBar(event.errorMessage)
                    }
                    is OverviewViewModel.CreateSessionEvent.Success -> {
                        loader.hide()
                        Log.d(TAG, "SESSION CREATED: ${event.createSessionResponse}")
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun addEmailChipToGroup(
        email: String,
    ) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && !emailList.contains(
                email
            )
        ) {
            emailList.add(email)

            val chip = Chip(requireContext())
            chip.text = email

            chip.isClickable = true
            chip.isCheckable = false
            chip.isCloseIconVisible = true

            binding.emailChipGroup.addView(chip)

            chip.setOnCloseIconClickListener {
                binding.emailChipGroup.removeView(chip)
                emailList.remove(email)
            }
        }
    }
}
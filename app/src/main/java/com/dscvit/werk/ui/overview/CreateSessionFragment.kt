package com.dscvit.werk.ui.overview

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dscvit.werk.R
import com.dscvit.werk.databinding.FragmentCreateSessionBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class CreateSessionFragment : Fragment() {
    private var _binding: FragmentCreateSessionBinding? = null
    private val binding get() = _binding!!

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

        val dateBuilder = MaterialDatePicker.Builder.datePicker()
        val datePicker = dateBuilder.build()

        binding.appBar.appBarTitle.text = requireContext().getString(R.string.create_a_session)
        binding.appBar.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.dateInput.editText!!.setOnClickListener {
            datePicker.show(childFragmentManager, dateBuilder.toString())

            datePicker.addOnPositiveButtonClickListener {
                Log.d(
                    "DatePickerActivity",
                    "Date String = ${datePicker.headerText}:: Date epoch value = $it"
                )
                binding.dateInput.editText!!.setText(datePicker.headerText)
            }
        }

        binding.timeInput.editText!!.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Session Time")
                .build()

            timePicker.show(childFragmentManager, timePicker.toString())
            timePicker.addOnPositiveButtonClickListener {
                binding.timeInput.editText!!.setText("${timePicker.hour} : ${timePicker.minute}")
            }
        }

        binding.nextButton.setOnClickListener {
            val action =
                CreateSessionFragmentDirections.actionCreateSessionFragmentToInviteFragment()
            findNavController().navigate(action)
        }
    }
}
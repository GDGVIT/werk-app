package com.dscvit.werk.ui.overview

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dscvit.werk.R
import com.dscvit.werk.databinding.FragmentCreateSessionBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

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

        binding.appBar.appBarTitle.text = requireContext().getString(R.string.create_a_session)
        binding.appBar.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        var startDateTimeStr = ""
        var endDateTimeStr = ""

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

        binding.nextButton.setOnClickListener {
            try {
                val formatter = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH)
                val startDateTime = formatter.parse(startDateTimeStr)
                val endDateTime = formatter.parse(endDateTimeStr)
                Log.d("BRR", startDateTime!!.time.toString())

//            val action =
//                CreateSessionFragmentDirections.actionCreateSessionFragmentToInviteFragment()
//            findNavController().navigate(action)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
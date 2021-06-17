package com.dscvit.werk.ui.tasks

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.werk.R
import com.dscvit.werk.databinding.ActivityTaskDescriptionBinding
import com.dscvit.werk.ui.adapter.MemberDialogAdapter

class TaskDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskDescriptionBinding
    private lateinit var membersAdapter: MemberDialogAdapter
    private lateinit var membersDialogBox: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDescriptionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.appBarTitle.text = "Task Name"

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.changeAssignedButton.setOnClickListener {
            membersDialogBox = Dialog(this)
            membersDialogBox.setContentView(R.layout.member_dialog_box)
            membersAdapter = MemberDialogAdapter()

            val recyclerView =
                membersDialogBox.findViewById<RecyclerView>(R.id.members_recycler_view)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = membersAdapter

            membersDialogBox.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            membersDialogBox.show()
        }
    }
}
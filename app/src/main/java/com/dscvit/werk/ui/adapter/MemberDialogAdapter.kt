package com.dscvit.werk.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.werk.R

class MemberDialogAdapter : RecyclerView.Adapter<MemberDialogAdapter.ViewHolder>() {
    private val nameList = listOf(
        "Rithik Jain",
        "Neil Kavalakkat",
        "Chanakya Vivek Kapoor",
        "Ram Gambhir",
        "Aryan Singh"
    )

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.member_dialog_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(nameList[position])
    }

    override fun getItemCount() = nameList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val memberName = view.findViewById<TextView>(R.id.member_name)

        fun bind(name: String) {
            memberName.text = name
        }
    }
}
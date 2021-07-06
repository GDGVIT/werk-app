package com.dscvit.werk.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dscvit.werk.R
import com.dscvit.werk.models.participants.Participant
import de.hdodenhof.circleimageview.CircleImageView

class MemberDialogAdapter : RecyclerView.Adapter<MemberDialogAdapter.ViewHolder>() {
    private var participants = mutableListOf<Participant>()

    fun setParticipants(participants: List<Participant>) {
        this.participants = participants as MutableList<Participant>
        notifyDataSetChanged()
    }

    fun getParticipants(position: Int): Participant {
        return participants[position]
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.member_dialog_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(participants[position])
    }

    override fun getItemCount() = participants.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val memberName = view.findViewById<TextView>(R.id.member_name)
        private val memberEmail = view.findViewById<TextView>(R.id.member_email)
        private val memberPhoto = view.findViewById<CircleImageView>(R.id.member_photo)

        fun bind(participant: Participant) {
            memberName.text = participant.name
            memberEmail.text = participant.email
            memberPhoto.load(participant.avatar)
        }
    }
}
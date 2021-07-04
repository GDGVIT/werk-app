package com.dscvit.werk.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.werk.R
import com.dscvit.werk.models.participants.Participant

class LeaderboardAdapter : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {
    private var participants = mutableListOf<Participant>()

    fun setParticipants(participants: List<Participant>) {
        this.participants = participants as MutableList<Participant>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.leaderboard_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(participants[position], position)
    }

    override fun getItemCount() = participants.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val numberText: TextView = view.findViewById(R.id.number_text)
        private val nameText: TextView = view.findViewById(R.id.name_text)
        private val emailText: TextView = view.findViewById(R.id.email_text)
        private val pointsText: TextView = view.findViewById(R.id.points_text)

        fun bind(participant: Participant, position: Int) {
            numberText.text = (position + 1).toString()
            nameText.text = participant.name
            emailText.text = participant.email
            pointsText.text = participant.points.toString()
        }
    }

}
package com.dscvit.werk.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.werk.R
import com.dscvit.werk.models.sessions.Session
import java.text.SimpleDateFormat
import java.util.*

class UpcomingSessionsAdapter : RecyclerView.Adapter<UpcomingSessionsAdapter.ViewHolder>() {

    private var sessionsList: MutableList<Session> = mutableListOf()

    fun updateSessions(newSessions: List<Session>) {
        sessionsList = newSessions as MutableList<Session>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.session_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(sessionsList[position])
    }

    override fun getItemCount() = sessionsList.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val sessionNameView = view.findViewById<TextView>(R.id.session_name_text)
        private val sessionDescView = view.findViewById<TextView>(R.id.session_desc_text)
        private val sessionTimeView = view.findViewById<TextView>(R.id.session_time_text)
        private val sessionDateView = view.findViewById<TextView>(R.id.session_date_text)

        fun bind(session: Session) {
            sessionNameView.text = session.sessionDetails.sessionName
            sessionDescView.text = session.sessionDetails.sessionDescription

            val startDate = Date(session.sessionDetails.startTime)
            val timeFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            sessionTimeView.text = timeFormat.format(startDate)
            sessionDateView.text = dateFormat.format(startDate)
        }
    }
}
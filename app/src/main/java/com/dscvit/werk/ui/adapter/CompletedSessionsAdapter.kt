package com.dscvit.werk.ui.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dscvit.werk.R
import com.dscvit.werk.models.sessions.Session
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class CompletedSessionsAdapter : RecyclerView.Adapter<CompletedSessionsAdapter.ViewHolder>() {

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
        private val userImg1 = view.findViewById<CircleImageView>(R.id.user_img_1)
        private val userImg2 = view.findViewById<CircleImageView>(R.id.user_img_2)
        private val userImg3 = view.findViewById<CircleImageView>(R.id.user_img_3)
        private val userImgList = listOf(userImg1, userImg2, userImg3)
        private val additionalUsers = view.findViewById<TextView>(R.id.additional_users)

        fun bind(session: Session) {
            sessionNameView.text = session.sessionDetails.sessionName
            sessionDescView.text = session.sessionDetails.sessionDescription

            val startDate = Date(session.sessionDetails.startTime)
            val timeFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            sessionTimeView.text = timeFormat.format(startDate)
            sessionDateView.text = dateFormat.format(startDate)

            if (session.participantsCount < 4) {
                additionalUsers.visibility = View.GONE
                for (i in 1..session.participantsCount) {
                    userImgList[i - 1].visibility = View.VISIBLE
                    userImgList[i - 1].load(session.participants[i - 1].avatar)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        userImgList[i - 1].tooltipText = session.participants[i - 1].name
                    }
                }
            } else {
                additionalUsers.visibility = View.VISIBLE
                additionalUsers.text = "${session.participantsCount - 3}"
                for (i in 0..2) {
                    userImgList[i].visibility = View.VISIBLE
                    userImgList[i - 1].load(session.participants[i - 1].avatar)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        userImgList[i - 1].tooltipText = session.participants[i - 1].name
                    }
                }
            }
        }
    }
}
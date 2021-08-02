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
import com.dscvit.werk.models.task.Task
import de.hdodenhof.circleimageview.CircleImageView

class AllTasksAdapter : RecyclerView.Adapter<AllTasksAdapter.ViewHolder>() {
    private var tasks = mutableListOf<Task>()

    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks as MutableList<Task>
        notifyDataSetChanged()
    }

    fun getTask(pos: Int): Task {
        return tasks[pos]
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.task_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(tasks[position])
    }

    override fun getItemCount() = tasks.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.task_title_text)
        private val description: TextView = view.findViewById(R.id.task_desc_text)
        private val assignedPhoto: CircleImageView = view.findViewById(R.id.assigned_photo)
        private val taskExpectedTime: TextView = view.findViewById(R.id.task_expected_time)

        fun bind(task: Task) {
            title.text = task.title
            description.text = task.description
            taskExpectedTime.text = "${task.expectedDuration} mins"
            if (task.assigned != null) {
                assignedPhoto.visibility = View.VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    assignedPhoto.tooltipText = task.assigned.email
                }
                assignedPhoto.load(task.assigned.avatar)
            } else {
                assignedPhoto.visibility = View.GONE
            }
        }
    }

}
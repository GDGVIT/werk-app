package com.dscvit.werk.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.werk.R
import com.dscvit.werk.models.task.TempTask

class AllTasksAdapter : RecyclerView.Adapter<AllTasksAdapter.ViewHolder>() {
    private var tasks = mutableListOf<TempTask>()

    fun setTasks(tasks: List<TempTask>) {
        this.tasks = tasks as MutableList<TempTask>
        notifyDataSetChanged()
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
        val title: TextView = view.findViewById(R.id.task_title_text)
        val description: TextView = view.findViewById(R.id.task_desc_text)

        fun bind(task: TempTask) {
            title.text = task.title
            description.text = task.description
        }
    }

}
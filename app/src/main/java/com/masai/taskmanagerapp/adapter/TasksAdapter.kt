package com.masai.taskmanagerapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.masai.taskmanagerapp.R
import com.masai.taskmanagerapp.models.Task

class TasksAdapter(val context: Context, val tasksList: MutableList<Task>) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(context)
        val view1: View = inflater.inflate(R.layout.task_item_row, parent, false)
        return TaskViewHolder(view1)

    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasksList.get(position)
        holder.title.text = task.title
        holder.desc.text = task.desc

    }

    override fun getItemCount(): Int {
        return tasksList.size
    }


    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var desc: TextView

        init {
            title = itemView.findViewById(R.id.tvTaskTitle)
            desc = itemView.findViewById(R.id.tvDesc)
        }
    }
}
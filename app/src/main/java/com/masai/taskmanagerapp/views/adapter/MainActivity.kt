package com.masai.taskmanagerapp.views.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.masai.taskmanagerapp.R
import com.masai.taskmanagerapp.models.local.Task
import com.masai.taskmanagerapp.models.local.TaskRoomDatabase
import com.masai.taskmanagerapp.models.local.TaskappDAO
import com.masai.taskmanagerapp.models.remote.requests.LoginRequestModel
import com.masai.taskmanagerapp.repository.TaskRepo
import com.masai.taskmanagerapp.viewmodels.TaskViewModel
import com.masai.taskmanagerapp.viewmodels.TaskViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnTaskItemClicked {

    lateinit var taskAdapter: TasksAdapter
    private val tasksList = mutableListOf<Task>()

    lateinit var roomDb: TaskRoomDatabase
    lateinit var taskDao: TaskappDAO
    lateinit var viewModel: TaskViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        roomDb = TaskRoomDatabase.getDatabaseObject(this)
        taskDao = roomDb.getTaskDAO()

        val repo = TaskRepo(taskDao)
        val viewModelFactory = TaskViewModelFactory(repo)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskViewModel::class.java)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            val newTask = Task("Dummy title", "Dummy desc")
            viewModel.addTask(newTask)
        }

        taskAdapter = TasksAdapter(this, tasksList, this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = taskAdapter

        viewModel.getTasksFromDB().observe(this, Observer {
            tasksList.clear()
            tasksList.addAll(it)
            taskAdapter.notifyDataSetChanged()
        })

        viewModel.getTasksFromAPI()

    }

    override fun onEditClicked(task: Task) {
        val newTitle = "New title"
        val newDesc = "New Desc"

        task.title = newTitle
        task.desc = newDesc

        viewModel.updateTask(task)
    }

    override fun onDeleteClicked(task: Task) {
        viewModel.deleteTask(task)
    }
}

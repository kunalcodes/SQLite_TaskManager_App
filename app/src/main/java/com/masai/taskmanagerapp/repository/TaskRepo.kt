package com.masai.taskmanagerapp.repository

import androidx.lifecycle.LiveData
import com.masai.taskmanagerapp.models.local.Task
import com.masai.taskmanagerapp.models.local.TaskappDAO
import com.masai.taskmanagerapp.models.remote.Network
import com.masai.taskmanagerapp.models.remote.Resource
import com.masai.taskmanagerapp.models.remote.ResponseHandler
import com.masai.taskmanagerapp.models.remote.TasksAPI
import com.masai.taskmanagerapp.models.remote.requests.LoginRequestModel
import com.masai.taskmanagerapp.models.remote.requests.LoginResponse
import com.masai.taskmanagerapp.models.remote.requests.SignupRequestModel
import com.masai.taskmanagerapp.models.remote.responses.GetTasksResponseModel
import com.masai.taskmanagerapp.models.remote.responses.SignupResponseModel
import kotlinx.coroutines.*
import java.lang.Exception

class TaskRepo(val taskDao: TaskappDAO) {

    private val api: TasksAPI = Network.getRetrofit().create(TasksAPI::class.java)
    private val responseHandler = ResponseHandler()
    private val token =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MGE0YmI3OTAzMjdlN2MwNmE2MTk1ODYiLCJpYXQiOjE2MzIxMzg2ODR9.cTxpYQrTfvramIOSPih6b1hJO_x1G-V2GmaRnTYSjU0"

    suspend fun login(loginRequestModel: LoginRequestModel): Resource<LoginResponse> {
        return try {
            val response = api.login(loginRequestModel)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    suspend fun signup(signupRequestModel: SignupRequestModel): Resource<SignupResponseModel> {
        return try {
            val response = api.signup(signupRequestModel)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    fun getRemoteTasks() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.getTasksFromAPI(token)
            saveToDB(response)
        }
    }

    private fun saveToDB(response: GetTasksResponseModel) {

        val listOfTasks = ArrayList<Task>()
        response.forEach {
            val newTask = Task(it.title, it.description)
            listOfTasks.add(newTask)
        }
        taskDao.deleteAll()
        taskDao.addTasks(listOfTasks)
    }

    fun addTaskToRoom(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDao.addTask(task)
        }
    }

    fun getAllTasks(): LiveData<List<Task>> {
        return taskDao.getTasks()
    }

    fun updateTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDao.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDao.delete(task)
        }
    }
}
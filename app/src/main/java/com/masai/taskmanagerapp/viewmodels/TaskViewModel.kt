package com.masai.taskmanagerapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.masai.taskmanagerapp.models.local.Task
import com.masai.taskmanagerapp.models.remote.Resource
import com.masai.taskmanagerapp.models.remote.requests.LoginRequestModel
import com.masai.taskmanagerapp.models.remote.requests.LoginResponse
import com.masai.taskmanagerapp.models.remote.requests.SignupRequestModel
import com.masai.taskmanagerapp.models.remote.responses.SignupResponseModel
import com.masai.taskmanagerapp.repository.TaskRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(val repo: TaskRepo) : ViewModel() {

    fun userLogin(loginRequestModel: LoginRequestModel): LiveData<Resource<LoginResponse>>{

        return liveData(Dispatchers.IO) {
            val result = repo.login(loginRequestModel)
            emit(result)
        }
    }

    fun userSignup(signupRequestModel: SignupRequestModel): LiveData<Resource<SignupResponseModel>>{

        return liveData(Dispatchers.IO) {
            val result = repo.signup(signupRequestModel)
            emit(result)
        }
    }

    fun getTasksFromAPI(){
        repo.getRemoteTasks()
    }

    fun addTask(task: Task){
        repo.addTaskToRoom(task)
    }

    fun getTasksFromDB(): LiveData<List<Task>> {
        return repo.getAllTasks()
    }

    fun updateTask(task: Task){
        repo.updateTask(task)
    }

    fun deleteTask(task: Task){
        repo.deleteTask(task)
    }
}
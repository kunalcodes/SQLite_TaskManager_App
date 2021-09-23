package com.masai.taskmanagerapp.views.adapter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.masai.taskmanagerapp.R
import com.masai.taskmanagerapp.models.local.TaskRoomDatabase
import com.masai.taskmanagerapp.models.local.TaskappDAO
import com.masai.taskmanagerapp.models.remote.Status
import com.masai.taskmanagerapp.models.remote.requests.LoginRequestModel
import com.masai.taskmanagerapp.models.remote.requests.SignupRequestModel
import com.masai.taskmanagerapp.repository.TaskRepo
import com.masai.taskmanagerapp.viewmodels.TaskViewModel
import com.masai.taskmanagerapp.viewmodels.TaskViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.longToast

class SignupActivity : AppCompatActivity() {


    lateinit var roomDb: TaskRoomDatabase
    lateinit var taskDao: TaskappDAO
    lateinit var viewModel: TaskViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        roomDb = TaskRoomDatabase.getDatabaseObject(this)
        taskDao = roomDb.getTaskDAO()

        val repo = TaskRepo(taskDao)
        val viewModelFactory = TaskViewModelFactory(repo)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskViewModel::class.java)

        val signupRequestModel = SignupRequestModel(
            "name",
            etSignUpEmail.text.toString(),
            etSignUpPassword.text.toString(),
            25
        )

        btnSignUpCreateAccount.setOnClickListener {
            viewModel.userSignup(signupRequestModel).observe(this, Observer {
                val response = it

                when (response.status) {
                    Status.SUCCESS -> {
                        openMainActivity()
                    }

                    Status.ERROR -> {
                        val error = response.message!!
                        longToast("error = $error")
                    }

                    Status.LOADING -> {

                    }
                }

            })
        }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
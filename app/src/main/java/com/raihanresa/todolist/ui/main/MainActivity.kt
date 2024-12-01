package com.raihanresa.todolist.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.raihanresa.todolist.data.local.UserPreference
import com.raihanresa.todolist.data.remote.ResultState
import com.raihanresa.todolist.databinding.ActivityMainBinding
import com.raihanresa.todolist.ui.adapter.TaskAdapter
import com.raihanresa.todolist.ui.task.AddTaskActivity
import com.raihanresa.todolist.ui.viewmodel.TaskViewModel
import com.raihanresa.todolist.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private val taskViewModel: TaskViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(this)

        setupRecyclerView()
        observeUserId()

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter()
        binding.recycleViewTask.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
        }
    }

    private fun observeUserId() {
        lifecycleScope.launch {
            val userId = userPreference.userIdFlow.first()
            if (userId != null) {
                setupObserver(userId)
            } else {
                Toast.makeText(this@MainActivity, "User ID not found!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObserver(userId: Int) {
        taskViewModel.getTaskById(userId).observe(this, Observer { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }
                is ResultState.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    val tasks = result.data.data
                    if (tasks.isNullOrEmpty()) {
                        binding.emptyImage.visibility = View.VISIBLE
                        binding.emptyText1.visibility = View.VISIBLE
                        binding.emptyText2.visibility = View.VISIBLE
                        binding.recycleViewTask.visibility = View.GONE
                    } else {
                        binding.emptyImage.visibility = View.GONE
                        binding.emptyText1.visibility = View.GONE
                        binding.emptyText2.visibility = View.GONE
                        binding.recycleViewTask.visibility = View.VISIBLE
                        taskAdapter.submitList(tasks)
                    }
                }
                is ResultState.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    val errorMessage = result.message.let {
                        try {
                            val json = JSONObject(it)
                            json.getString("errors")
                        } catch (e: JSONException) {
                            it
                        }
                    } ?: "An error occurred"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
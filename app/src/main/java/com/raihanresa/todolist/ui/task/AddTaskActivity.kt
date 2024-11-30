package com.raihanresa.todolist.ui.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raihanresa.todolist.databinding.ActivityAddTaskBinding
import com.raihanresa.todolist.ui.main.MainActivity

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        binding.sendButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}
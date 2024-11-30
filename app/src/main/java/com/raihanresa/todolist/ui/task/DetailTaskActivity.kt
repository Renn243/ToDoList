package com.raihanresa.todolist.ui.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raihanresa.todolist.databinding.ActivityDetailTaskBinding
import com.raihanresa.todolist.ui.main.MainActivity

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        binding.editButton.setOnClickListener {

        }
    }
}
package com.raihanresa.todolist.ui.task

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import com.raihanresa.todolist.R
import com.raihanresa.todolist.databinding.ActivityAddTaskBinding
import com.raihanresa.todolist.ui.main.MainActivity
import java.util.Calendar

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        binding.addTimer.setOnClickListener {
            openDateTimePicker()
        }

        binding.addPriority.setOnClickListener {
            openPriorityDialog()
        }

        binding.addCategory.setOnClickListener {

        }

        binding.addTimer.addTextChangedListener {
            if (!TextUtils.isEmpty(binding.addTimer.text)) {
                binding.addTimer.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            } else {
                binding.addTimer.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_24, 0, 0, 0)
            }
        }

        binding.addPriority.addTextChangedListener {
            if (!TextUtils.isEmpty(binding.addPriority.text)) {
                binding.addPriority.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            } else {
                binding.addPriority.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_24, 0, 0, 0)
            }
        }
    }

    private fun openDateTimePicker() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            TimePickerDialog(this, { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                val formattedDate = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
                val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                val formattedDateTime = "$formattedDate, $formattedTime"

                binding.addTimer.text = formattedDateTime
                binding.addTimer.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun openPriorityDialog() {
        val dialog = Dialog(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.priority_selection, null)
        dialog.setContentView(dialogView)

        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val editButton = dialogView.findViewById<Button>(R.id.saveButton)

        val priority1 = dialogView.findViewById<Button>(R.id.priority1)
        val priority2 = dialogView.findViewById<Button>(R.id.priority2)
        val priority3 = dialogView.findViewById<Button>(R.id.priority3)

        var selectedPriority: Int? = null

        priority1.setOnClickListener {
            selectedPriority = 1
            priority1.setBackgroundResource(R.drawable.priority_button_pressed)
            priority2.setBackgroundResource(R.drawable.label2)
            priority3.setBackgroundResource(R.drawable.label2)
        }

        priority2.setOnClickListener {
            selectedPriority = 2
            priority2.setBackgroundResource(R.drawable.priority_button_pressed)
            priority1.setBackgroundResource(R.drawable.label2)
            priority3.setBackgroundResource(R.drawable.label2)
        }

        priority3.setOnClickListener {
            selectedPriority = 3
            priority3.setBackgroundResource(R.drawable.priority_button_pressed)
            priority1.setBackgroundResource(R.drawable.label2)
            priority2.setBackgroundResource(R.drawable.label2)
        }

        cancelButton.setOnClickListener { dialog.dismiss() }
        editButton.setOnClickListener {
            selectedPriority?.let {
                binding.addPriority.text = it.toString()
            }
            dialog.dismiss()
        }
        dialog.show()
    }
}
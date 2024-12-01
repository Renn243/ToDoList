package com.raihanresa.todolist.ui.task

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.raihanresa.todolist.R
import com.raihanresa.todolist.data.local.UserPreference
import com.raihanresa.todolist.data.remote.ResultState
import com.raihanresa.todolist.databinding.ActivityAddTaskBinding
import com.raihanresa.todolist.ui.main.MainActivity
import com.raihanresa.todolist.ui.viewmodel.TaskViewModel
import com.raihanresa.todolist.ui.viewmodel.ViewModelFactory
import org.json.JSONException
import org.json.JSONObject
import java.util.Calendar

class AddTaskActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(this)

        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        binding.sendButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            val time = binding.addTimer.text.toString()
            val priority = binding.addPriority.text.toString()
            val category = binding.addCategory.text.toString()

            lifecycleScope.launchWhenStarted {
                userPreference.userIdFlow.collect { id ->
                    if (id != null) {
                        taskViewModel.addTask(title, description, time, priority, category, id)
                            .observe(this@AddTaskActivity) { result ->
                                when (result) {
                                    is ResultState.Loading -> {
                                        binding.progressIndicator.visibility = View.VISIBLE
                                    }
                                    is ResultState.Success -> {
                                        binding.progressIndicator.visibility = View.GONE
                                        startActivity(Intent(this@AddTaskActivity, MainActivity::class.java))
                                        finishAffinity()
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
                                        val dialog = AlertDialog.Builder(this@AddTaskActivity)
                                            .setMessage(errorMessage)
                                            .setPositiveButton("OK", null)
                                            .create()
                                        dialog.show()
                                    }
                                }
                            }
                    } else {
                        Toast.makeText(this@AddTaskActivity, "User ID not found!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



        binding.addTimer.setOnClickListener {
            openDateTimePicker()
        }

        binding.addPriority.setOnClickListener {
            openPriorityDialog()
        }

        binding.addCategory.setOnClickListener {
            openCategoryDialog()
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

        binding.addCategory.addTextChangedListener {
            if (!TextUtils.isEmpty(binding.addCategory.text)) {
                binding.addCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            } else {
                binding.addCategory.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_24, 0, 0, 0)
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

    @SuppressLint("InflateParams")
    private fun openPriorityDialog() {
        val dialog = Dialog(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.priority_selection, null)
        dialog.setContentView(dialogView)

        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val saveButton = dialogView.findViewById<Button>(R.id.saveButton)

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
        saveButton.setOnClickListener {
            selectedPriority?.let {
                binding.addPriority.text = it.toString()
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("InflateParams")
    private fun openCategoryDialog() {
        val dialog = Dialog(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.category_selection, null)
        dialog.setContentView(dialogView)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams

        val addCategoryButton = dialogView.findViewById<Button>(R.id.addCategoryButton)

        val groceryButton = dialogView.findViewById<LinearLayout>(R.id.groceryButton)
        val workButton = dialogView.findViewById<LinearLayout>(R.id.workButton)
        val sportButton = dialogView.findViewById<LinearLayout>(R.id.sportButton)
        val designButton = dialogView.findViewById<LinearLayout>(R.id.designButton)
        val universityButton = dialogView.findViewById<LinearLayout>(R.id.universityButton)
        val socialButton = dialogView.findViewById<LinearLayout>(R.id.socialButton)
        val musicButton = dialogView.findViewById<LinearLayout>(R.id.musicButton)
        val healthButton = dialogView.findViewById<LinearLayout>(R.id.healthButton)
        val movieButton = dialogView.findViewById<LinearLayout>(R.id.movieButton)

        val groceryText = groceryButton.findViewById<TextView>(R.id.groceryText)
        val workText = workButton.findViewById<TextView>(R.id.workText)
        val sportText = sportButton.findViewById<TextView>(R.id.sportText)
        val designText = designButton.findViewById<TextView>(R.id.designText)
        val universityText = universityButton.findViewById<TextView>(R.id.universityText)
        val socialText = socialButton.findViewById<TextView>(R.id.socialText)
        val musicText = musicButton.findViewById<TextView>(R.id.musicText)
        val healthText = healthButton.findViewById<TextView>(R.id.healthText)
        val movieText = movieButton.findViewById<TextView>(R.id.movieText)

        // Fungsi untuk memberikan underline pada teks
        fun setUnderline(textView: TextView) {
            val spannable = SpannableString(textView.text)
            spannable.setSpan(UnderlineSpan(), 0, spannable.length, 0)
            textView.text = spannable
        }

        fun removeUnderlines(vararg textViews: TextView) {
            textViews.forEach {
                val spannable = SpannableString(it.text)
                it.text = spannable
            }
        }

        var selectedCategory: String? = null

        groceryButton.setOnClickListener {
            removeUnderlines(groceryText, workText, sportText, designText, universityText, socialText, musicText, healthText, movieText)
            setUnderline(groceryText)
            selectedCategory = groceryText.text.toString()
        }

        workButton.setOnClickListener {
            removeUnderlines(groceryText, workText, sportText, designText, universityText, socialText, musicText, healthText, movieText)
            setUnderline(workText)
            selectedCategory = workText.text.toString()
        }

        sportButton.setOnClickListener {
            removeUnderlines(groceryText, workText, sportText, designText, universityText, socialText, musicText, healthText, movieText)
            setUnderline(sportText)
            selectedCategory = sportText.text.toString()
        }

        designButton.setOnClickListener {
            removeUnderlines(groceryText, workText, sportText, designText, universityText, socialText, musicText, healthText, movieText)
            setUnderline(designText)
            selectedCategory = designText.text.toString()
        }

        universityButton.setOnClickListener {
            removeUnderlines(groceryText, workText, sportText, designText, universityText, socialText, musicText, healthText, movieText)
            setUnderline(universityText)
            selectedCategory = universityText.text.toString()
        }

        socialButton.setOnClickListener {
            removeUnderlines(groceryText, workText, sportText, designText, universityText, socialText, musicText, healthText, movieText)
            setUnderline(socialText)
            selectedCategory = socialText.text.toString()
        }

        musicButton.setOnClickListener {
            removeUnderlines(groceryText, workText, sportText, designText, universityText, socialText, musicText, healthText, movieText)
            setUnderline(musicText)
            selectedCategory = musicText.text.toString()
        }

        healthButton.setOnClickListener {
            removeUnderlines(groceryText, workText, sportText, designText, universityText, socialText, musicText, healthText, movieText)
            setUnderline(healthText)
            selectedCategory = healthText.text.toString()
        }

        movieButton.setOnClickListener {
            removeUnderlines(groceryText, workText, sportText, designText, universityText, socialText, musicText, healthText, movieText)
            setUnderline(movieText)
            selectedCategory = movieText.text.toString()
        }

        addCategoryButton.setOnClickListener {
            selectedCategory?.let {
                binding.addCategory.text = it
            }
            dialog.dismiss()
        }

        dialog.show()
    }
}
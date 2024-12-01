package com.raihanresa.todolist.ui.task

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.raihanresa.todolist.R
import com.raihanresa.todolist.data.remote.ResultState
import com.raihanresa.todolist.databinding.ActivityDetailTaskBinding
import com.raihanresa.todolist.ui.main.MainActivity
import com.raihanresa.todolist.ui.viewmodel.TaskViewModel
import com.raihanresa.todolist.ui.viewmodel.ViewModelFactory
import org.json.JSONException
import org.json.JSONObject

class DetailTaskActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityDetailTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val time= intent.getStringExtra("time")
        val priority = intent.getStringExtra("priority")
        val category = intent.getStringExtra("category")
        val id = intent.getIntExtra("id", 0)

        binding.title.text = title
        binding.description.text = description
        binding.addCategory.text = category
        binding.addPriority.text = priority
        binding.addTimer.text = time

        category?.let {
            val drawable = getDrawableForCategory(category)
            binding.addCategory.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)

            val color = ContextCompat.getColor(this, getColorForCategory(category))
            binding.addCategory.backgroundTintList = ColorStateList.valueOf(color)
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        binding.deleteButton.setOnClickListener {
            // Menampilkan dialog konfirmasi sebelum menghapus task
            val dialog = AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes") { _, _ ->
                    // Melakukan penghapusan task setelah konfirmasi
                    taskViewModel.deleteTask(id).observe(this) { result ->
                        when (result) {
                            is ResultState.Loading -> {
                                binding.progressIndicator.visibility = View.VISIBLE
                            }

                            is ResultState.Success -> {
                                binding.progressIndicator.visibility = View.GONE

                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
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
                                val errorDialog = AlertDialog.Builder(this)
                                    .setMessage(errorMessage)
                                    .setPositiveButton("OK", null)
                                    .create()
                                errorDialog.show()
                            }
                        }
                    }
                }
                .setNegativeButton("No", null) // Tombol untuk membatalkan
                .create()

            dialog.show()
        }

        binding.editButton.setOnClickListener {
            val intent = Intent(this, EditTaskActivity::class.java).apply {
                putExtra("id", id)
                putExtra("title", title)
                putExtra("description", description)
                putExtra("time", time)
                putExtra("priority", priority)
                putExtra("category", category)
            }
            startActivity(intent)
        }
    }

    private fun getDrawableForCategory(category: String): Int {
        return when (category) {
            "Grocery" -> R.drawable.grocery
            "Work" -> R.drawable.work
            "Sport" -> R.drawable.sport
            "Design" -> R.drawable.design
            "University" -> R.drawable.university
            "Social" -> R.drawable.social
            "Music" -> R.drawable.music
            "Health" -> R.drawable.health
            "Movie" -> R.drawable.movie
            else -> R.drawable.baseline_add_24
        }
    }

    private fun getColorForCategory(category: String): Int {
        return when (category) {
            "Grocery" -> R.color.grocery
            "Work" -> R.color.work
            "Sport" -> R.color.sport
            "Design" -> R.color.design
            "University" -> R.color.university
            "Social" -> R.color.social
            "Music" -> R.color.music
            "health" -> R.color.health
            "movie" -> R.color.movie
            else -> R.color.black
        }
    }
}
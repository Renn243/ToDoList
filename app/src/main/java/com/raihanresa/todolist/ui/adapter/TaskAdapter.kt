package com.raihanresa.todolist.ui.adapter

import android.content.Intent
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raihanresa.todolist.R
import com.raihanresa.todolist.data.remote.response.DataItem
import com.raihanresa.todolist.databinding.ItemTaskBinding
import com.raihanresa.todolist.ui.task.DetailTaskActivity

class TaskAdapter : ListAdapter<DataItem, TaskAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    class MyViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: DataItem) {
            binding.taskTitle.text = task.title
            binding.taskCategory.text = task.category ?: "Unknown" // Nilai default jika null
            binding.taskPriority.text = task.priority
            binding.taskTime.text = task.time

            val category = task.category ?: "Unknown"

            val drawable = getDrawableForCategory(category)
            binding.taskCategory.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)

            val color = ContextCompat.getColor(itemView.context, getColorForCategory(category))
            binding.taskCategory.backgroundTintList = ColorStateList.valueOf(color)

            Log.d("TaskAdapter", "Color: ${getColorForCategory(category)}")

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailTaskActivity::class.java).apply {
                    putExtra("title", task.title)
                    putExtra("description", task.description)
                    putExtra("category", task.category)
                    putExtra("time", task.time)
                    putExtra("priority", task.priority)
                    putExtra("id", task.id)
                }
                itemView.context.startActivity(intent)
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

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
package com.raihanresa.todolist.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.raihanresa.todolist.data.repository.TaskRepository

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    fun getTaskById(id: Int) = taskRepository.getTaskById(id)

    fun addTask(title: String, description: String, time: String, priority: String, category: String, userId: Int) =
        taskRepository.addTask(title, description, time, priority, category, userId)

    fun editTask(title: String, description: String, time: String, priority: String, category: String, taskId: Int) =
        taskRepository.editTask(title, description, time, priority, category, taskId)

    fun deleteTask(userId: Int) = taskRepository.deleteTask(userId)
}
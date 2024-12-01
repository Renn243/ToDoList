package com.raihanresa.todolist.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.raihanresa.todolist.data.repository.TaskRepository

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    fun getTaskById(id: Int) = taskRepository.getTaskById(id)

}
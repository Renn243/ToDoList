package com.raihanresa.todolist.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raihanresa.todolist.data.repository.TaskRepository
import com.raihanresa.todolist.data.repository.UserRepository
import com.raihanresa.todolist.di.Injection

class ViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(TaskViewModel::class.java) -> {
                TaskViewModel(taskRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            val userRepository = Injection.provideUserRepository()
            val taskRepository = Injection.provideTaskRepository()
            return INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(userRepository, taskRepository)
                    .also { INSTANCE = it }
            }
        }
    }
}
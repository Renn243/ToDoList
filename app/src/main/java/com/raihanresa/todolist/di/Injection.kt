package com.raihanresa.todolist.di

import com.raihanresa.todolist.data.remote.retrofit.ApiConfig
import com.raihanresa.todolist.data.repository.TaskRepository
import com.raihanresa.todolist.data.repository.UserRepository

object Injection {
    fun provideUserRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
    fun provideTaskRepository(): TaskRepository {
        val apiService = ApiConfig.getApiService()
        return TaskRepository.getInstance(apiService)
    }
}
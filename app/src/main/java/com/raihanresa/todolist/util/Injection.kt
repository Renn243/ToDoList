package com.raihanresa.todolist.util

import com.raihanresa.todolist.data.remote.retrofit.ApiConfig
import com.raihanresa.todolist.data.repository.UserRepository

object Injection {
    fun provideUserRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}
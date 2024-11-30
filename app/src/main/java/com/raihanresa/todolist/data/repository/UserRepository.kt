package com.raihanresa.todolist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.raihanresa.todolist.data.remote.ResultState
import com.raihanresa.todolist.data.remote.request.LoginRequest
import com.raihanresa.todolist.data.remote.response.LoginResponse
import com.raihanresa.todolist.data.remote.request.RegisterRequest
import com.raihanresa.todolist.data.remote.retrofit.ApiService
import com.raihanresa.todolist.data.remote.response.RegisterResponse


class UserRepository(private val apiService: ApiService) {

    fun login(email: String, password: String): LiveData<ResultState<LoginResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val request = LoginRequest(email, password)
                val response = apiService.login(request)
                if (response.isSuccessful) {
                    emit(ResultState.Success(response.body()!!))
                } else {
                    emit(ResultState.Error(response.errorBody()?.string() ?: "An error occurred"))
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e.message ?: "An error occurred"))
            }
        }

    fun register(name: String, username: String, password: String): LiveData<ResultState<RegisterResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val request = RegisterRequest(username, password)
                val response = apiService.register(request)
                if (response.isSuccessful) {
                    emit(ResultState.Success(response.body()!!))
                } else {
                    emit(ResultState.Error(response.errorBody()?.string() ?: "An error occurred"))
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e.message ?: "An error occurred"))
            }
        }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}
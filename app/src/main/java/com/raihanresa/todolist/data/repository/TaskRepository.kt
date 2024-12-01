package com.raihanresa.todolist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.raihanresa.todolist.data.remote.ResultState
import com.raihanresa.todolist.data.remote.request.AddTaskRequest
import com.raihanresa.todolist.data.remote.response.AddTaskResponse
import com.raihanresa.todolist.data.remote.response.DeleteTaskResponse
import com.raihanresa.todolist.data.remote.response.UserTaskResponse
import com.raihanresa.todolist.data.remote.retrofit.ApiService

class TaskRepository(private val apiService: ApiService) {

    fun getTaskById(id: Int): LiveData<ResultState<UserTaskResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.getTaskById(id)
                if (response.isSuccessful) {
                    emit(ResultState.Success(response.body()!!))
                } else {
                    emit(ResultState.Error(response.errorBody()?.string() ?: "An error occurred"))
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e.message ?: "An error occurred"))
            }
        }

    fun addTask(title: String, description: String, time: String, priority: String, category: String, userId: Int ): LiveData<ResultState<AddTaskResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val request = AddTaskRequest(title, description, time, priority, category, userId)
                val response = apiService.addTask(request)
                if (response.isSuccessful) {
                    emit(ResultState.Success(response.body()!!))
                } else {
                    emit(ResultState.Error(response.errorBody()?.string() ?: "An error occurred"))
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e.message ?: "An error occurred"))
            }
        }

    fun deleteTask(userId: Int ): LiveData<ResultState<DeleteTaskResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.deleteTask(userId)
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
        private var instance: TaskRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): TaskRepository =
            instance ?: synchronized(this) {
                instance ?: TaskRepository(apiService)
            }.also { instance = it }
    }
}
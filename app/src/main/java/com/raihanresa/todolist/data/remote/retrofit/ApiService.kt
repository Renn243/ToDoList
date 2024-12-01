package com.raihanresa.todolist.data.remote.retrofit

import com.raihanresa.todolist.data.remote.request.AddTaskRequest
import com.raihanresa.todolist.data.remote.request.LoginRequest
import com.raihanresa.todolist.data.remote.request.RegisterRequest
import com.raihanresa.todolist.data.remote.response.AddTaskResponse
import com.raihanresa.todolist.data.remote.response.DeleteTaskResponse
import com.raihanresa.todolist.data.remote.response.LoginResponse
import com.raihanresa.todolist.data.remote.response.RegisterResponse
import com.raihanresa.todolist.data.remote.response.UserTaskResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("tasks")
    suspend fun addTask(
        @Body request: AddTaskRequest
    ): Response<AddTaskResponse>

    @GET("tasks/user/{id}")
    suspend fun getTaskById(@Path("id") id: Int): Response<UserTaskResponse>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Int): Response<DeleteTaskResponse>

}
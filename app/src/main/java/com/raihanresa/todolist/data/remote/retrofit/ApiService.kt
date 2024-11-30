package com.raihanresa.todolist.data.remote.retrofit

import com.raihanresa.todolist.data.remote.request.LoginRequest
import com.raihanresa.todolist.data.remote.request.RegisterRequest
import com.raihanresa.todolist.data.remote.response.LoginResponse
import com.raihanresa.todolist.data.remote.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

}
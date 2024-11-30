package com.raihanresa.todolist.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.raihanresa.todolist.data.repository.UserRepository

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun login(username: String, password: String) = userRepository.login(username, password)

    fun register(username: String, password: String) = userRepository.register(username, password)
}
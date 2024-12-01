package com.raihanresa.todolist.data.remote.request

data class AddTaskRequest (
    val title: String,
    val description: String,
    val time: String,
    val priority: String,
    val category: String,
    val userId: Int
)
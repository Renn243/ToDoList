package com.raihanresa.todolist.data.remote.request

data class EditTaskRequest (
    val title: String,
    val description: String,
    val time: String,
    val priority: String,
    val category: String
)
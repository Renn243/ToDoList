package com.raihanresa.todolist.data.remote.response

import com.google.gson.annotations.SerializedName

data class DeleteTaskResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

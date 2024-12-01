package com.raihanresa.todolist.data.remote.response

import com.google.gson.annotations.SerializedName

data class EditTaskResponse(

	@field:SerializedName("data")
	val data: DataEdit? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataEdit(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("priority")
	val priority: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("userId")
	val userId: Int? = null
)

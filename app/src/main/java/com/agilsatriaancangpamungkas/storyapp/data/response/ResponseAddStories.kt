package com.agilsatriaancangpamungkas.storyapp.data.response

import com.google.gson.annotations.SerializedName

data class ResponseAddStories(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

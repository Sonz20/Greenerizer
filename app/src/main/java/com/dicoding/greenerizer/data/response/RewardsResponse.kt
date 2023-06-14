package com.dicoding.greenerizer.data.response

import com.google.gson.annotations.SerializedName

data class RewardsResponse(

	@field:SerializedName("RewardsResponse")
	val rewardsResponse: List<RewardsResponseItem>
)

data class RewardsResponseItem(

	@field:SerializedName("idreward")
	val idreward: Int,

	@field:SerializedName("urlgambar")
	val urlgambar: String,

	@field:SerializedName("hargareward")
	val hargareward: Int,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("namareward")
	val namareward: String
)

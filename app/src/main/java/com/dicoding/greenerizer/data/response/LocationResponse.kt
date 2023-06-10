package com.dicoding.greenerizer.data.response

import com.google.gson.annotations.SerializedName

data class LocationResponse(

	@field:SerializedName("LocationResponse")
	val locationResponse: List<LocationResponseItem>
)

data class LocationResponseItem(

	@field:SerializedName("latitude")
	val latitude: String,

	@field:SerializedName("namabanksampah")
	val namabanksampah: String,

	@field:SerializedName("idlokasi")
	val idlokasi: Int,

	@field:SerializedName("longitude")
	val longitude: String
)

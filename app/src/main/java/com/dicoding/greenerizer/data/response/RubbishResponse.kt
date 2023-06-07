package com.dicoding.greenerizer.data.response

import com.google.gson.annotations.SerializedName

data class RubbishResponse(

	@field:SerializedName("RubbishResponse")
	val rubbishResponse: List<RubbishResponseItem?>? = null
)

data class RubbishResponseItem(

	@field:SerializedName("idsampah")
	val idsampah: Int? = null,

	@field:SerializedName("harga_perkilo_sampah")
	val hargaPerkiloSampah: Int? = null,

	@field:SerializedName("panduan_pengelolaan_sampah")
	val panduanPengelolaanSampah: String? = null,

	@field:SerializedName("jenis_sampah")
	val jenisSampah: String? = null,

	@field:SerializedName("deskripsi_sampah")
	val deskripsiSampah: String? = null
)

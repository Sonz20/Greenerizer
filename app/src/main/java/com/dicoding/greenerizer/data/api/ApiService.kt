package com.dicoding.greenerizer.data.api

import com.dicoding.greenerizer.data.response.LocationResponseItem
import com.dicoding.greenerizer.data.response.RewardsResponseItem
import com.dicoding.greenerizer.data.response.RubbishResponseItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("api/db/sampah")
    suspend fun getRubbish(): Response<List<RubbishResponseItem>>

    @GET("api/db/lokasi")
    suspend fun getRubbishBankLocation(): Response<List<LocationResponseItem>>

    @GET("api/db/rewards")
    suspend fun getRewards(): Response<List<RewardsResponseItem>>
}
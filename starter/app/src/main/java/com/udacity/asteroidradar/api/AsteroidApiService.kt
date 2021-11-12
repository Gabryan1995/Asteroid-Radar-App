package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.Constants.YOUR_API_KEY
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Asteroid Network Interface
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getProperties(@Query("start_date") startDate: String,
                              @Query("end_date") endDate: String,
                              @Query("api_key") apiKey: String): String
}

object AsteroidApi {
    val retrofitService : AsteroidApiService by lazy { retrofit.create(AsteroidApiService::class.java) }
}

// PotD Network Interface
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitMoshi = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface PotdApiService {
    @GET("planetary/apod?api_key=$YOUR_API_KEY")
    suspend fun getPotD(): PictureOfDay
}

object PotdApi {
    val retrofitMoshiService : PotdApiService by lazy { retrofitMoshi.create(PotdApiService::class.java) }
}
package com.udacity.asteroidradar.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.DatabaseAsteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception

class AsteroidRepository(private val database: AsteroidDatabase) {
    @RequiresApi(Build.VERSION_CODES.N)
    val dates = getNextSevenDaysFormattedDates()
    val startDate = dates[0]
    val endDate = dates[7]

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDatabaseDao.getAsteroids()) {
            it.asDomainModel()
        }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroidList = AsteroidApi.retrofitService.getProperties(startDate, endDate, Constants.YOUR_API_KEY)

                val formattedAsteroidList = parseAsteroidsJsonResult(JSONObject(asteroidList.asteroids.toString()))

                database.asteroidDatabaseDao.insertAll(*formattedAsteroidList.asDatabaseModel())
            } catch (e: Exception) {
                Log.i("AsteroidsRepository","Failure: $e")
            }
        }
    }
}
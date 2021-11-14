package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.*
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.PotdApi
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

enum class AsteroidApiStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _potd = MutableLiveData<PictureOfDay>()
    val potd: LiveData<PictureOfDay>
        get() = _potd

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }

    val asteroids = asteroidRepository.asteroids

    fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                _potd.value = PotdApi.retrofitMoshiService.getPotD()

                Log.i("AsteroidsImage", "Success: ${_potd.value!!.url}")
            } catch (e: Exception) {
                Log.i("AsteroidsImage", "Failure: ${e.message}")
            }
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
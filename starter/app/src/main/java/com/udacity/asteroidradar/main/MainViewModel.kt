package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.*
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.PotdApi
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

enum class AsteroidApiStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

//    private val _asteroids = MutableLiveData<List<Asteroid>>()
//    val asteroids: LiveData<List<Asteroid>>
//        get() = _asteroids

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

//    private fun getMarsRealEstateProperties() {
//        viewModelScope.launch {
//            _status.value = AsteroidApiStatus.LOADING
//            try {
//                _asteroids.value = AsteroidApi.retrofitService.getProperties()
//                _status.value = AsteroidApiStatus.DONE
//            } catch (e: Exception) {
//                _status.value = AsteroidApiStatus.ERROR
//                _asteroids.value = ArrayList()
//            }
//        }
//    }

    fun getPictureOfDay(imageView: ImageView, titleTextView: TextView) {
        viewModelScope.launch {
            try {
                var potd = PotdApi.retrofitMoshiService.getPotD()

                Picasso.with(getApplication<Application>().applicationContext)
                    .load(potd.url)
                    .into(imageView)

                titleTextView.text = potd.title

                Log.i("AsteroidsImage", "Success: ${potd.url}")
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
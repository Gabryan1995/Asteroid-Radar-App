package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.detail.DetailViewModel
import com.udacity.asteroidradar.main.MainViewModel

class DetailViewModelFactory (private val asteroid: Asteroid, private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return DetailViewModel(asteroid, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
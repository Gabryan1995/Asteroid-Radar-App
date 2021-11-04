package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {
    private val _properties = MutableLiveData<List<Asteroid>>()

    val properties: LiveData<List<Asteroid>>
        get() = _properties

    private val _navigateToSelectedProperty = MutableLiveData<Asteroid?>()

    val navigateToSelectedProperty: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedProperty

    fun displayPropertyDetails(asteroid: Asteroid) {
        _navigateToSelectedProperty.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }
}
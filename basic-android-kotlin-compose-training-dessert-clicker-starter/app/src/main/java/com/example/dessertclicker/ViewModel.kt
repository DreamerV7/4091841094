// ViewModel.kt в папке model/
package com.example.dessertclicker.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.R

class DessertViewModel : ViewModel() {

    private val _revenue = MutableStateFlow(0)
    val revenue: StateFlow<Int> = _revenue.asStateFlow()

    private val _dessertsSold = MutableStateFlow(0)
    val dessertsSold: StateFlow<Int> = _dessertsSold.asStateFlow()

    private val _currentDessert = MutableStateFlow(Dessert(R.drawable.cupcake, 5, 0))
    val currentDessert: StateFlow<Dessert> = _currentDessert.asStateFlow()

    private val desserts = Datasource.dessertList

    fun onDessertClicked() {
        // Update revenue and desserts sold
        _revenue.value += _currentDessert.value.price
        _dessertsSold.value += 1


        updateCurrentDessert()
    }

    private fun updateCurrentDessert() {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (_dessertsSold.value >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                break
            }
        }
        _currentDessert.value = dessertToShow
    }
}
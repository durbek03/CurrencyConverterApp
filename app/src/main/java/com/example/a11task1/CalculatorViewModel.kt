package com.example.a11task1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.a11task1.database.entity.RoomCurrency

class CalculatorViewModel : ViewModel() {
    var multiplier = MutableLiveData<Double>()

    fun setMultiplier(multiplier: Double) {
        this.multiplier.value = multiplier
    }
}
package com.example.a11task1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.a11task1.database.entity.RoomCurrency

class AllCurrencyViewModel : ViewModel() {
    var list = MutableLiveData<List<RoomCurrency>>()
    var searchList = MutableLiveData<List<RoomCurrency>>()

    fun setData(list: List<RoomCurrency>) {
        this.list.value = list
    }

    fun setSearchList(list: List<RoomCurrency>) {
        searchList.value = list
    }
}
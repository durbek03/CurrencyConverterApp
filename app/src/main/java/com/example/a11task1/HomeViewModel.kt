package com.example.a11task1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a11task1.database.entity.RoomCurrency
import com.example.a11task1.database.entity.SortedCurrency

class HomeViewModel : ViewModel() {
    var dataList = MutableLiveData<List<RoomCurrency>>()
    var historyList = MutableLiveData<List<RoomCurrency>>()

    fun setData(data: List<RoomCurrency>) {
        dataList.value = data
    }

    fun setHistory(historyList: List<RoomCurrency>) {
        this.historyList.value = historyList
    }
}
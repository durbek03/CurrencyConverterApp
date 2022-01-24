package com.example.a11task1.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SortedCurrency {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    @ColumnInfo
    var currencyName: String? = null

    constructor()
    constructor(currencyName: String?) {
        this.currencyName = currencyName
    }
}
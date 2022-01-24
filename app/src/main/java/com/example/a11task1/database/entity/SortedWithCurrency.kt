package com.example.a11task1.database.entity

import androidx.room.Embedded
import androidx.room.Relation

class SortedWithCurrency (
    @Embedded val sortedCurrency: SortedCurrency,
    @Relation(
        parentColumn = "currencyName",
        entityColumn = "title"
    )
    val currencyList: List<RoomCurrency>
)
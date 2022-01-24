package com.example.a11task1.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapInfo
import androidx.room.Query
import com.example.a11task1.database.entity.RoomCurrency
import com.example.a11task1.database.entity.SortedCurrency
import com.example.a11task1.database.entity.SortedWithCurrency
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface SortedCurrencyDao {

    @Insert
    suspend fun addSortedCurrency(sortedCurrency: SortedCurrency)

    @Query("select  * from sortedcurrency")
    suspend fun getAllData(): List<SortedWithCurrency>
}
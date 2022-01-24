package com.example.a11task1.database.dao

import androidx.room.*
import com.example.a11task1.database.entity.RoomCurrency
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface CurrencyDao {
    @Insert
    suspend fun addCurrency(roomCurrency: RoomCurrency)

    @Query("delete from roomcurrency")
    fun deleteAllCurrency(): Completable
}
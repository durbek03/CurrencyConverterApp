package com.example.a11task1.database.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a11task1.database.dao.CurrencyDao
import com.example.a11task1.database.dao.SortedCurrencyDao
import com.example.a11task1.database.entity.RoomCurrency
import com.example.a11task1.database.entity.SortedCurrency

@Database(entities = [RoomCurrency::class, SortedCurrency::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
    abstract fun sortedCurrencyDao(): SortedCurrencyDao

    companion object {
        private var appDatabase: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "currencydatabase")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return appDatabase!!
        }
    }
}
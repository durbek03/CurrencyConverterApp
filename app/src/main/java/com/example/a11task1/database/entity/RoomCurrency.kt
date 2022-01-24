package com.example.a11task1.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class RoomCurrency : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    @ColumnInfo
    var cb_price: String? = null
    @ColumnInfo
    var code: String? = null
    @ColumnInfo
    var date: String? = null
    @ColumnInfo
    var nbu_buy_price: String? = null
    @ColumnInfo
    var nbu_cell_price: String? = null
    @ColumnInfo
    var title: String? = null

    constructor()
    constructor(
        cb_price: String?,
        code: String?,
        date: String?,
        nbu_buy_price: String?,
        nbu_cell_price: String?,
        title: String?
    ) {
        this.cb_price = cb_price
        this.code = code
        this.date = date
        this.nbu_buy_price = nbu_buy_price
        this.nbu_cell_price = nbu_cell_price
        this.title = title
    }
}
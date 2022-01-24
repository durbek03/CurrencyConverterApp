package com.example.a11task1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.example.a11task1.R
import com.example.a11task1.database.entity.RoomCurrency
import com.example.a11task1.databinding.SpinnerItemBinding
import com.squareup.picasso.Picasso

class SpinnerAdapter(val list: List<RoomCurrency>): BaseAdapter() {
    val BASE_URL = "https://flagcdn.com/w1280/"
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): RoomCurrency {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val binder = DataBindingUtil.inflate<SpinnerItemBinding>(
            LayoutInflater.from(p2?.context), R.layout.spinner_item, p2, false)
        val currency = list[p0]
        binder.currency = currency
        val path = BASE_URL+"${currency.code?.substring(0, 2)?.lowercase()}.png"
        Picasso.get().load(path).into(binder.flag)
        binder.currencyName.text = currency.code
        return binder.root
    }
}
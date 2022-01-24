package com.example.a11task1.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.a11task1.R
import com.example.a11task1.database.entity.RoomCurrency
import com.example.a11task1.databinding.AllCurrencyRvItemBinding
import com.squareup.picasso.Picasso

class AllCurrencyRvAdapter(val clicker: MyCalcClickListener): ListAdapter<RoomCurrency, AllCurrencyRvAdapter.Vh>(AllCurrencyDiffUtil()) {
    private val TAG = "AllCurrencyRvAdapter"
    val BASE_URL = "https://flagcdn.com/w1280/"
    inner class Vh(val binding: AllCurrencyRvItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(currency: RoomCurrency) {
            binding.currency = currency
            val path = BASE_URL+"${currency.code?.substring(0, 2)?.lowercase()}.png"
            Log.d(TAG, "onBind: $path")
            Picasso.get().load(path).into(binding.flag)
            binding.calc.setOnClickListener {
                clicker.onClick(currency)
            }
        }
    }

    class AllCurrencyDiffUtil(): DiffUtil.ItemCallback<RoomCurrency>() {
        override fun areItemsTheSame(oldItem: RoomCurrency, newItem: RoomCurrency): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RoomCurrency, newItem: RoomCurrency): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.all_currency_rv_item, parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    interface MyCalcClickListener {
        fun onClick(roomCurrency: RoomCurrency)
    }
}
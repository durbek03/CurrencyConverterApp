package com.example.a11task1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.a11task1.R
import com.example.a11task1.database.entity.RoomCurrency
import com.example.a11task1.databinding.HistoryRvItemBinding

class HistoryRvAdapter: ListAdapter<RoomCurrency, HistoryRvAdapter.Vh>(MyDiffUtils()) {
    inner class Vh(val binding: HistoryRvItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(currency: RoomCurrency) {
            binding.currency = currency
        }
    }

    class MyDiffUtils: DiffUtil.ItemCallback<RoomCurrency>() {
        override fun areItemsTheSame(oldItem: RoomCurrency, newItem: RoomCurrency): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RoomCurrency, newItem: RoomCurrency): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.history_rv_item, parent, false)!!)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }
}
package com.example.a11task1.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.a11task1.R
import com.example.a11task1.database.entity.RoomCurrency
import com.example.a11task1.databinding.ViewpagerCardLayoutBinding

class ViewPagerAdapter : ListAdapter<RoomCurrency, ViewPagerAdapter.Vh>(MyDiffUtil()) {

    inner class Vh(val binder: ViewpagerCardLayoutBinding) : RecyclerView.ViewHolder(binder.root) {
        fun onBind(currency: RoomCurrency) {
            binder.currency = currency
        }
    }

    class MyDiffUtil : DiffUtil.ItemCallback<RoomCurrency>() {
        override fun areItemsTheSame(oldItem: RoomCurrency, newItem: RoomCurrency): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RoomCurrency, newItem: RoomCurrency): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.viewpager_card_layout, parent, false)!!)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }
}
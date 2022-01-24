package com.example.a11task1

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.a11task1.adapters.HistoryRvAdapter
import com.example.a11task1.adapters.ViewPagerAdapter
import com.example.a11task1.database.database.AppDatabase
import com.example.a11task1.database.entity.RoomCurrency
import com.example.a11task1.database.entity.SortedWithCurrency
import com.example.a11task1.databinding.HomeFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableEmitter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.internal.operators.flowable.FlowableFromArray
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"

    companion object {
        fun newInstance() = HomeFragment()
    }
    private lateinit var viewModel: HomeViewModel
    lateinit var appDatabase: AppDatabase
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var historyRvAdapter: HistoryRvAdapter
    lateinit var binding: HomeFragmentBinding
    var historyCurrencyList = listOf<SortedWithCurrency>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.home_fragment, container, false)
        binding = HomeFragmentBinding.bind(root)

        val job = lifecycleScope.launch(Dispatchers.IO) {
            val job = async {  appDatabase.sortedCurrencyDao().getAllData() }
            val initialList = mutableListOf<RoomCurrency>()
            historyCurrencyList = job.await()
            for (i in job.await()) {
                initialList.add(i.currencyList.last())
            }
            withContext(Dispatchers.Main) {
                viewModel.setData(initialList)
            }
        }

        viewPagerAdapter = ViewPagerAdapter()
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (historyCurrencyList.isEmpty()) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        job.join()
                        val history = historyCurrencyList[position]
                        viewModel.setHistory(history.currencyList)
                    }
                } else {
                    val history = historyCurrencyList[position]
                    viewModel.setHistory(history.currencyList)
                }
                super.onPageSelected(position)
            }
        })

        viewModel.dataList.observe(viewLifecycleOwner, Observer {
            viewPagerAdapter.submitList(it.toList())
        })

        historyRvAdapter = HistoryRvAdapter()
        binding.rv.adapter = historyRvAdapter
        viewModel.historyList.observe(viewLifecycleOwner, {
            historyRvAdapter.submitList(it.asReversed())
        })

        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab, position ->
            tab.text = viewModel.dataList.value!![position].code
        }.attach()

        TabLayoutMediator(
            binding.dottedTabLayout, binding.viewPager
        ) { tab, position ->
        }.attach()

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        appDatabase = AppDatabase.getInstance(requireContext())
    }
}
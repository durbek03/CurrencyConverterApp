package com.example.a11task1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.a11task1.databinding.ActivityMainBinding
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.annotation.NonNull
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.a11task1.adapters.AllCurrencyRvAdapter
import com.example.a11task1.database.database.AppDatabase
import com.example.a11task1.database.entity.RoomCurrency
import com.example.a11task1.database.entity.SortedCurrency
import com.example.a11task1.retrofit.RetrofitClient
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import kotlin.Exception

class MainActivity : AppCompatActivity(), AllCurrencyRvAdapter.MyCalcClickListener, ToolbarService {
    private val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var viewModel: ActivityViewModel
    lateinit var appDatabase: AppDatabase
    val homeFragment = HomeFragment.newInstance()
    val allCurrencyFragment = AllCurrencyFragment.newInstance()
    val calculatorFragment = CalculatorFragment.newInstance()
    val retrofit = RetrofitClient.getRetrofit("https://nbu.uz/")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ActivityViewModel::class.java]
        appDatabase = AppDatabase.getInstance(this)

        val toolbar = binding.toolbarLayout.toolbar
        toolbar.setNavigationOnClickListener {
            binding.apply {
                drawerLayout.openDrawer(navView)
            }
        }

        binding.botNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.main -> {viewModel.setFragment(homeFragment)}
                R.id.all -> {viewModel.setFragment(allCurrencyFragment)}
                R.id.calculator -> {viewModel.setFragment(calculatorFragment)}
            }
            true
        }

        addData()

        viewModel.activeFragment.observe(this, Observer {
            val beginTransaction = supportFragmentManager.beginTransaction()
            beginTransaction.replace(R.id.frag_container, it).commit()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(binding.navView)) {
            binding.drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    fun addData() {
        if (!NetworkHelper(this).isNetworkConnected()) {
            Toast.makeText(this, "Iltimos internetni yoqib qayta kiring, malumotlar yangilanmasligi mumkun", Toast.LENGTH_SHORT).show()
            return
        }
        val job = lifecycleScope.launch(Dispatchers.IO) {
            val response = retrofit.getCurrency().awaitResponse()
            if (response.isSuccessful) {
                val apiCurrency = response.body()
                val allData = async { appDatabase.sortedCurrencyDao().getAllData() }
                val sortedCurrencyList = allData.await()

                if (sortedCurrencyList.isEmpty()) {
                    for (currency in apiCurrency!!) {
                        val sortedCurrency = SortedCurrency(currency.title)
                        appDatabase.sortedCurrencyDao().addSortedCurrency(sortedCurrency)
                        val roomCurrency = RoomCurrency(
                            currency.cb_price,
                            currency.code,
                            currency.date,
                            currency.nbu_buy_price,
                            currency.nbu_cell_price,
                            currency.title
                        )
                        appDatabase.currencyDao().addCurrency(roomCurrency)
                        Log.d(TAG, "addData: onProcess")
                    }
                } else {
                    val currencyToCheck = apiCurrency!![0]
                    for (sorted in sortedCurrencyList) {
                        if (sorted.sortedCurrency.currencyName == currencyToCheck.title) {
                            val sortedCurrency = sorted.currencyList[sorted.currencyList.lastIndex]
                            if (currencyToCheck.date == sortedCurrency.date) {
                            } else {
                                for (currency in apiCurrency) {
                                    val roomCurrency = RoomCurrency(
                                        currency.cb_price,
                                        currency.code,
                                        currency.date,
                                        currency.nbu_buy_price,
                                        currency.nbu_cell_price,
                                        currency.title
                                    )
                                    appDatabase.currencyDao().addCurrency(roomCurrency)
                                }
                            }
                        }
                    }
                }
            }
        }
        lifecycleScope.launch(Dispatchers.Main) {
            job.join()
            Log.d(TAG, "addData: DONE")
            if (viewModel.activeFragment.value == null) {
                viewModel.setFragment(homeFragment)
            }
        }
    }

    override fun onClick(roomCurrency: RoomCurrency) {
        calculatorFragment.arguments = Bundle().apply { putSerializable("currency", roomCurrency) }
        viewModel.setFragment(calculatorFragment)
        binding.botNavView.selectedItemId = R.id.calculator
    }

    override fun getToolbar(): MaterialToolbar {
        return binding.toolbarLayout.toolbar
    }
}
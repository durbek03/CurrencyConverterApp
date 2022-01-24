package com.example.a11task1

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.a11task1.adapters.SpinnerAdapter
import com.example.a11task1.database.database.AppDatabase
import com.example.a11task1.database.entity.RoomCurrency
import com.example.a11task1.databinding.AllCurrencyFragmentBinding
import com.example.a11task1.databinding.CalculatorFragmentBinding
import com.example.a11task1.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.lang.Exception

class CalculatorFragment : Fragment() {
    private val TAG = "CalculatorFragment"

    companion object {
        fun newInstance() = CalculatorFragment()
    }

    private lateinit var viewModel: CalculatorViewModel
    lateinit var spinnerAdapter: SpinnerAdapter
    lateinit var bindging: CalculatorFragmentBinding
    lateinit var appDatabase: AppDatabase
    var currencyList = listOf<RoomCurrency>()
    var args: RoomCurrency? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindging = DataBindingUtil.inflate(inflater, R.layout.calculator_fragment, container, false)

        lifecycleScope.launch(Dispatchers.IO) {
            val job = async { appDatabase.sortedCurrencyDao().getAllData() }
            val initialList = mutableListOf<RoomCurrency>()
            for (i in job.await()) {
                initialList.add(i.currencyList.last())
            }
            val uzb = RoomCurrency("1", "UZS", "date", "1", "1", "O'zbek so'mi")
            initialList.add(0, uzb)
            currencyList = initialList
            withContext(Dispatchers.Main) {
                spinnerAdapter = SpinnerAdapter(initialList)
                bindging.fromCurrencySpinner.adapter = spinnerAdapter
                bindging.toCurrencySpinner.adapter = spinnerAdapter

                if (args == null) {
                    for (i in initialList) {
                        if (i.code == "USD") {
                            bindging.fromCurrencySpinner.setSelection(initialList.indexOf(i))
                        }
                    }
                } else {
                    for (i in initialList) {
                        if (i.code == args!!.code) {
                            bindging.fromCurrencySpinner.setSelection(initialList.indexOf(i))
                        }
                    }
                }
            }
        }

        bindging.fromCurrencySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    setMultiplier()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        bindging.toCurrencySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    setMultiplier()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        bindging.change.setOnClickListener {
            changePositions()
        }

        bindging.fromCurrencyEt.addTextChangedListener {
            try {
                setCalculated()
            } catch (e: Exception) {
                Toast.makeText(context, "Iltimos qabul qilinsa bo'ladigan malumot", Toast.LENGTH_SHORT).show()
                return@addTextChangedListener
            }
        }

        viewModel.multiplier.observe(viewLifecycleOwner, {
            setCalculated()
        })

        return bindging.root
    }

    fun setCalculated() {
        if (bindging.fromCurrencyEt.text.toString().isNotEmpty()) {
            val from = bindging.fromCurrencyEt.text.toString().toDouble()
            val to = from * viewModel.multiplier.value!!
            bindging.sellPrice.text = to.toString()
            bindging.buyPrice.text = to.toString()
        } else {
            bindging.sellPrice.text = ""
            bindging.buyPrice.text = ""
        }
    }

    private fun setMultiplier() {
        val firstPos = bindging.fromCurrencySpinner.selectedItemPosition
        val firstCur = currencyList[firstPos]
        val secondPos = bindging.toCurrencySpinner.selectedItemPosition
        val secondCur = currencyList[secondPos]

        val url =
            "https://currency-exchange.p.rapidapi.com/"
        val retrofit = RetrofitClient.getRetrofit(url)
        lifecycleScope.launch(Dispatchers.IO) {
            val response = retrofit.getMultiplier(firstCur.code!!, secondCur.code!!, "1.0")
            withContext(Dispatchers.Main) {
                val multi = response.body()
                if (response.isSuccessful && multi != null) {
                    viewModel.setMultiplier(multi)
                }
            }
        }
    }

    fun changePositions() {
        val fromPos = bindging.fromCurrencySpinner.selectedItemPosition
        val toPos = bindging.toCurrencySpinner.selectedItemPosition

        bindging.fromCurrencySpinner.setSelection(toPos)
        bindging.toCurrencySpinner.setSelection(fromPos)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalculatorViewModel::class.java)
        args = arguments?.getSerializable("currency") as? RoomCurrency
        appDatabase = AppDatabase.getInstance(requireContext())
    }

}
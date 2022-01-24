package com.example.a11task1

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.a11task1.adapters.AllCurrencyRvAdapter
import com.example.a11task1.database.database.AppDatabase
import com.example.a11task1.database.entity.RoomCurrency
import com.example.a11task1.databinding.AllCurrencyFragmentBinding
import com.example.a11task1.databinding.ToolbarBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllCurrencyFragment : Fragment() {
    private val TAG = "AllCurrencyFragment"
    lateinit var appDatabase: AppDatabase
    lateinit var binding: AllCurrencyFragmentBinding
    private lateinit var viewModel: AllCurrencyViewModel
    lateinit var rvAdapter: AllCurrencyRvAdapter
    lateinit var toolbarService: ToolbarService
    lateinit var calcClickListener: AllCurrencyRvAdapter.MyCalcClickListener
    var textChangeListener: TextWatcher? = null

    companion object {
        fun newInstance() = AllCurrencyFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.all_currency_fragment, container, false)
        binding = AllCurrencyFragmentBinding.bind(root)
        val toolbar = toolbarService.getToolbar()
        val menu = toolbar.menu
        val children = menu.children
        for (i in children) {
            if (i.itemId == R.id.search) {
                i.isVisible = true
            }
        }


        lifecycleScope.launch(Dispatchers.IO) {
            val job = async { appDatabase.sortedCurrencyDao().getAllData() }
            val initialList = arrayListOf<RoomCurrency>()
            for (i in job.await()) {
                initialList.add(i.currencyList.last())
            }
            withContext(Dispatchers.Main) {
                viewModel.setData(initialList)
            }
            Log.d(TAG, "onCreateView: ${initialList.size}")
        }

        rvAdapter = AllCurrencyRvAdapter(object : AllCurrencyRvAdapter.MyCalcClickListener {
            override fun onClick(roomCurrency: RoomCurrency) {
                calcClickListener.onClick(roomCurrency)
            }
        })
        binding.rv.adapter = rvAdapter
        viewModel.list.observe(viewLifecycleOwner, {
            rvAdapter.submitList(it)
        })

        toolbar.setOnMenuItemClickListener {
            toolbar.visibility = View.GONE
            binding.searchLayout.visibility = View.VISIBLE
            true
        }

        binding.icReturn.setOnClickListener {
            rvAdapter.submitList(viewModel.list.value)
            binding.searchLayout.visibility = View.GONE
            toolbar.visibility = View.VISIBLE
        }

        textChangeListener = binding.seachEt.addTextChangedListener {
            if (it.isNullOrEmpty()) return@addTextChangedListener
            val list = viewModel.list.value
            val resultList = mutableListOf<RoomCurrency>()
            if (list != null) {
                for (i in list) {
                    if (i.code!!.contains(it.toString(), ignoreCase = true)) {
                        resultList.add(i)
                    }
                }
            }
            viewModel.searchList.value = resultList.toList()
        }

        viewModel.searchList.observe(viewLifecycleOwner, {
            rvAdapter.submitList(it)
        })
        return root
    }

    override fun onAttach(context: Context) {
        toolbarService = context as ToolbarService
        calcClickListener = context as AllCurrencyRvAdapter.MyCalcClickListener
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        viewModel = ViewModelProvider(this)[AllCurrencyViewModel::class.java]
    }

    override fun onDestroyView() {
        val toolbar = toolbarService.getToolbar()
        toolbar.visibility = View.VISIBLE
        binding.searchLayout.visibility = View.GONE
        val menu = toolbar.menu
        val children = menu.children
        for (i in children) {
            if (i.itemId == R.id.search) {
                i.isVisible = false
            }
        }
        binding.seachEt.setText("")
        binding.seachEt.removeTextChangedListener(textChangeListener)
        super.onDestroyView()
    }

}
package com.example.a11task1

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActivityViewModel: ViewModel() {
    val activeFragment = MutableLiveData<Fragment>()

    fun setFragment(fragment: Fragment) {
        activeFragment.value = fragment
    }
}
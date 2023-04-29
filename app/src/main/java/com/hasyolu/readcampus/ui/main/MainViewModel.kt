package com.hasyolu.readcampus.ui.main

import androidx.hilt.Assisted
import androidx.lifecycle.*
import com.hasyolu.readcampus.base.BaseViewModel
import com.hasyolu.readcampus.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    init {
        LogUtil.i("init MainViewModel")
    }
}
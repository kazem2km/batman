package com.yara.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yara.common.base.BaseViewModel

/**
 * A simple [BaseViewModel] that provide the data and handle logic to communicate with the model
 * for [DetailImageFragment].
 */
class DetailImageViewModel: BaseViewModel() {

    // PRIVATE DATA
    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> get() = _imageUrl

    // PUBLIC ACTIONS ---
    fun loadDataWhenFragmentStarts(imageUrl: String) {
        _imageUrl.value = imageUrl
    }
}
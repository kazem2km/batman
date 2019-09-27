package com.yara.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yara.common.base.BaseViewModel
import com.yara.common.utils.Event
import com.yara.detail.domain.GetUserDetailUseCase
import com.yara.model.Search
import com.yara.repository.AppDispatchers
import com.yara.repository.utils.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [BaseViewModel] that provide the data and handle logic to communicate with the model
 * for [DetailFragment].
 */
class DetailViewModel(private val getUserDetailUseCase: GetUserDetailUseCase,
                      private val dispatchers: AppDispatchers
): BaseViewModel() {

    // PRIVATE DATA
    private lateinit var argsLogin: String
    private var searchSource: LiveData<Resource<Search>> = MutableLiveData()

    private val _user = MediatorLiveData<Search>()
    val search: LiveData<Search> get() = _user
    private val _isLoading = MutableLiveData<Resource.Status>()
    val isLoading: LiveData<Resource.Status> get() = _isLoading

    // PUBLIC ACTIONS ---
    fun loadDataWhenActivityStarts(login: String) {
        argsLogin = login
        getUserDetail(false)
    }

    fun reloadDataWhenUserRefreshes()
            = getUserDetail(true)

    fun userClicksOnAvatarImage(search: Search)
            = navigate(
        DetailFragmentDirections.actionDetailFragmentToImageDetailFragment(
            search.Poster
        )
    )

    // ---

    private fun getUserDetail(forceRefresh: Boolean) = viewModelScope.launch(dispatchers.main) {
        _user.removeSource(searchSource) // We make sure there is only one source of livedata (allowing us properly refresh)
        withContext(dispatchers.io) { searchSource = getUserDetailUseCase(forceRefresh = forceRefresh, login = argsLogin) }
        _user.addSource(searchSource) {
            _user.value = it.data
            _isLoading.value = it.status
            if (it.status == Resource.Status.ERROR) _snackbarError.value = Event(R.string.an_error_happened)
        }
    }
}
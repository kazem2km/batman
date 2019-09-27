package com.yara.home

import androidx.lifecycle.*
import com.yara.common.base.BaseViewModel
import com.yara.common.utils.Event
import com.yara.home.domain.GetTopUsersUseCase
import com.yara.model.Search
import com.yara.repository.AppDispatchers
import com.yara.repository.utils.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [BaseViewModel] that provide the data and handle logic to communicate with the model
 * for [HomeFragment].
 */
class HomeViewModel(private val getTopUsersUseCase: GetTopUsersUseCase,
                    private val dispatchers: AppDispatchers
) : BaseViewModel() {

    // FOR DATA
    private val _searches = MediatorLiveData<Resource<List<Search>>>()
    val searches: LiveData<Resource<List<Search>>> get() = _searches
    private var searchesSource: LiveData<Resource<List<Search>>> = MutableLiveData()

    init {
        getUsers(false)
    }

    // PUBLIC ACTIONS ---
    fun userClicksOnItem(search: Search)
            = navigate(
        HomeFragmentDirections.actionHomeFragmentToDetailFragment(
            search.imdbID
        )
    )

    fun userRefreshesItems()
            = getUsers(true)

    // ---

    private fun getUsers(forceRefresh: Boolean) = viewModelScope.launch(dispatchers.main) {
        _searches.removeSource(searchesSource) // We make sure there is only one source of livedata (allowing us properly refresh)
        withContext(dispatchers.io) { searchesSource = getTopUsersUseCase(forceRefresh = forceRefresh) }
        _searches.addSource(searchesSource) {
            _searches.value = it
            if (it.status == Resource.Status.ERROR) _snackbarError.value = Event(R.string.an_error_happened)
        }
    }
}
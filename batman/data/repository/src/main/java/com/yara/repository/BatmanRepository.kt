package com.yara.repository

import androidx.lifecycle.LiveData
import com.yara.local.dao.SearchDao
import com.yara.model.ApiResult
import com.yara.model.Search
import com.yara.remote.BatmanDatasource
import com.yara.repository.utils.NetworkBoundResource
import com.yara.repository.utils.Resource
import kotlinx.coroutines.Deferred

interface BatmanRepository {
    suspend fun getBatmansWithCache(forceRefresh: Boolean = false): LiveData<Resource<List<Search>>>
    suspend fun getBatmanDetailWithCache(forceRefresh: Boolean = false, id: String): LiveData<Resource<Search>>
}

class BatmanRepositoryImpl(private val datasource: BatmanDatasource,
                           private val dao: SearchDao
): BatmanRepository {

    /**
     * Suspended function that will get a list of top [Search]
     * whether in cache (SQLite) or via network (API).
     * [NetworkBoundResource] is responsible to handle this behavior.
     */
    override suspend fun getBatmansWithCache(forceRefresh: Boolean): LiveData<Resource<List<Search>>> {
        return object : NetworkBoundResource<List<Search>, ApiResult<Search>>() {

            override fun processResponse(response: ApiResult<Search>): List<Search>
                    = response.Search

            override suspend fun saveCallResults(items: List<Search>)
                    = dao.save(items)

            override fun shouldFetch(data: List<Search>?): Boolean
                    = data == null || data.isEmpty() || forceRefresh

            override suspend fun loadFromDb(): List<Search>
                    = dao.getTopUsers()

            override fun createCallAsync(): Deferred<ApiResult<Search>>
                    = datasource.fetchBatmansAsync()

        }.build().asLiveData()
    }

    /**
     * Suspended function that will get details of a [Search]
     * whether in cache (SQLite) or via network (API).
     * [NetworkBoundResource] is responsible to handle this behavior.
     */
    override suspend fun getBatmanDetailWithCache(forceRefresh: Boolean, id: String): LiveData<Resource<Search>> {
        return object : NetworkBoundResource<Search, Search>() {

            override fun processResponse(response: Search): Search
                    = response

            override suspend fun saveCallResults(item: Search)
                    = dao.save(item)

            override fun shouldFetch(data: Search?): Boolean
                    = data == null
                    || data.haveToRefreshFromNetwork()
                    || data.Title.isNullOrEmpty()
                    || forceRefresh

            override suspend fun loadFromDb(): Search
                    = dao.getUser(id)

            override fun createCallAsync(): Deferred<Search>
                    = datasource.fetchBatmanDetailsAsync(id)

        }.build().asLiveData()
    }
}
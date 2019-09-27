package com.yara.remote

import com.yara.model.ApiResult
import com.yara.model.Search
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BatmanService {

    @GET("/")
    fun fetchBatmansAsync(@Query("apikey") query: String = "3e974fca",
                          @Query("s") sort: String = "batman"): Deferred<ApiResult<Search>>

    @GET("/")
    fun fetchBatmanDetailsAsync(@Query("apikey") query: String = "3e974fca",
        @Query("i") id: String): Deferred<Search>
}
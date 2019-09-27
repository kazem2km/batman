package com.yara.model

import com.google.gson.annotations.SerializedName

data class ApiResult<T>(@SerializedName("Response")
                        val Response: String,
                        @SerializedName("Search")
                        val Search: List<T>,
                        @SerializedName("totalResults")
                        val totalResults: String)
package com.yara.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*
import java.util.concurrent.TimeUnit

@Entity
data class Search (
    @SerializedName("Poster")
    val Poster: String,
    @SerializedName("Title")
    val Title: String,
    @SerializedName("Type")
    val Type: String,
    @SerializedName("Year")
    val Year: String,
    @PrimaryKey
    @SerializedName("imdbID")
    val imdbID: String,

    var lastRefreshed: Date
) {
    /**
     * We consider that an [Search] is outdated when the last time
     * we fetched it was more than 10 minutes
     */
    fun haveToRefreshFromNetwork() : Boolean
            = TimeUnit.MILLISECONDS.toMinutes(Date().time - lastRefreshed.time) >= 10
}
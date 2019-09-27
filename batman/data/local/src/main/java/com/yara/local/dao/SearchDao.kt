package com.yara.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.yara.model.Search
import java.util.*

@Dao
abstract class SearchDao: BaseDao<Search>() {

    @Query("SELECT * FROM Search ORDER BY imdbID ASC LIMIT 30")
    abstract suspend fun getTopUsers(): List<Search>

    @Query("SELECT * FROM Search WHERE imdbID = :login LIMIT 1")
    abstract suspend fun getUser(login: String): Search

    // ---

    /**
     * Each time we save an search, we update its 'lastRefreshed' field
     * This allows us to know when we have to refresh its data
     */

    suspend fun save(search: Search) {
        insert(search.apply { lastRefreshed = Date() })
    }

    suspend fun save(searches: List<Search>) {
        insert(searches.apply { forEach { it.lastRefreshed = Date() } })
    }
}
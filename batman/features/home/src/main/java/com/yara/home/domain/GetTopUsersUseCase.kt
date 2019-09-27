package com.yara.home.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.yara.model.Search
import com.yara.repository.utils.Resource
import com.yara.repository.BatmanRepository

/**
 * Use case that gets a [Resource][List][Search] from [BatmanRepository]
 * and makes some specific logic actions on it.
 *
 * In this Use Case, I'm just doing nothing... ¯\_(ツ)_/¯
 */
class GetTopUsersUseCase(private val repository: BatmanRepository) {

    suspend operator fun invoke(forceRefresh: Boolean = false): LiveData<Resource<List<Search>>> {
        return Transformations.map(repository.getBatmansWithCache(forceRefresh)) {
            it // Place here your specific logic actions
        }
    }
}
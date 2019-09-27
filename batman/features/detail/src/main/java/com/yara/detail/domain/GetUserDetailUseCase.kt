package com.yara.detail.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.yara.model.Search
import com.yara.repository.utils.Resource
import com.yara.repository.BatmanRepository

/**
 * Use case that gets a [Resource] [Search] from [BatmanRepository]
 * and makes some specific logic actions on it.
 *
 * In this Use Case, I'm just doing nothing... ¯\_(ツ)_/¯
 */
class GetUserDetailUseCase(private val repository: BatmanRepository) {

    suspend operator fun invoke(forceRefresh: Boolean = false, login: String): LiveData<Resource<Search>> {
        return Transformations.map(repository.getBatmanDetailWithCache(forceRefresh, login)) {
            it // Place here your specific logic actions (if any)
        }
    }
}
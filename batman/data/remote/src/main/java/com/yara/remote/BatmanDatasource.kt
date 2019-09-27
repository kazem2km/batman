package com.yara.remote

/**
 * Implementation of [BatmanService] interface
 */
class BatmanDatasource(private val batmanService: BatmanService) {

    fun fetchBatmansAsync() =
            batmanService.fetchBatmansAsync()

    fun fetchBatmanDetailsAsync(id: String) =
            batmanService.fetchBatmanDetailsAsync(id= id)
}
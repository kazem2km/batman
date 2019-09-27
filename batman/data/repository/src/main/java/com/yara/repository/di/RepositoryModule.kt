package com.yara.repository.di

import com.yara.repository.AppDispatchers
import com.yara.repository.BatmanRepository
import com.yara.repository.BatmanRepositoryImpl
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module.module

val repositoryModule = module {
    factory { AppDispatchers(Dispatchers.Main, Dispatchers.IO) }
    factory { BatmanRepositoryImpl(
        get(),
        get()
    ) as BatmanRepository
    }
}
package com.yara.detail.di

import com.yara.detail.DetailImageViewModel
import com.yara.detail.DetailViewModel
import com.yara.detail.domain.GetUserDetailUseCase
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val featureDetailModule = module {
    factory { GetUserDetailUseCase(get()) }
    viewModel { DetailViewModel(get(), get()) }
    viewModel { DetailImageViewModel() }
}
package com.yara.batman.di

import com.yara.detail.di.featureDetailModule
import com.yara.home.di.featureHomeModule
import com.yara.local.di.localModule
import com.yara.remote.di.createRemoteModule
import com.yara.repository.di.repositoryModule

val appComponent= listOf(
    createRemoteModule("http://www.omdbapi.com"),
    repositoryModule,
    featureHomeModule, featureDetailModule, localModule
)
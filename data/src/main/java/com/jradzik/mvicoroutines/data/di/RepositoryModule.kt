package com.jradzik.mvicoroutines.data.di

import com.jradzik.mvicoroutines.data.feature.DemoDataSource
import com.jradzik.mvicoroutines.data.feature.DemoRepositoryImpl
import com.jradzik.mvicoroutines.domain.feature.demo.DemoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    fun provideDemoRepository(
        demoDataSource: DemoDataSource
    ): DemoRepository = DemoRepositoryImpl(demoDataSource)
}
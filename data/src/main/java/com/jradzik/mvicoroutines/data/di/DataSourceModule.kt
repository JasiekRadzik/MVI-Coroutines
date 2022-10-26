package com.jradzik.mvicoroutines.data.di

import com.jradzik.mvicoroutines.data.feature.DemoDataSource
import com.jradzik.mvicoroutines.data.feature.DemoDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DataSourceModule {

    @Provides
    fun provideDemoDataSource(): DemoDataSource = DemoDataSourceImpl()
}

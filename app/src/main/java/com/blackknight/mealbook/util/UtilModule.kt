package com.blackknight.mealbook.util

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilModule {
    @Binds
    @Singleton
    abstract fun schedulerProvider(schedulerProviderImpl: SchedulerProviderImpl): SchedulerProvider
}
package com.blackknight.mealbook.data.repo

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun categoryRepo(categoryRepoImpl: CategoryRepoImpl): CategoryRepo
}
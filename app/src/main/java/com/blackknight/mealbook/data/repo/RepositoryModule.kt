package com.blackknight.mealbook.data.repo

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun categoryRepo(categoryRepoImpl: CategoryRepoImpl): CategoryRepo

    @Binds
    @Singleton
    abstract fun mealRepo(mealRepoImpl: MealRepoImpl): MealRepo
}
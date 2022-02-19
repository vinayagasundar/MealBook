package com.blackknight.mealbook.data.repo

import com.blackknight.mealbook.data.daos.CategoryDao
import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.mapper.Mapper
import com.blackknight.mealbook.network.MealDBService
import com.blackknight.mealbook.network.response.CategoryListResponse
import com.blackknight.mealbook.network.response.CategoryResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CategoryRepoImplTest {

    @Mock
    private lateinit var categoryDao: CategoryDao

    @Mock
    private lateinit var mealDBService: MealDBService

    @Mock
    private lateinit var mapper: Mapper<CategoryResponse, Category>

    private lateinit var categoryRepo: CategoryRepoImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        categoryRepo = CategoryRepoImpl(categoryDao, mealDBService, mapper)
    }

    @Test
    fun `when getCategories invoked and dao return empty list and call api service `() {
        val categoryResponse = CategoryResponse(
            id = "id",
            name = "name",
            thumbnail = "thumbnail",
            description = "description"
        )
        val response = CategoryListResponse(list = listOf(categoryResponse))
        val category = Category(
            id = "id",
            name = "name",
            thumbnail = "thumbnail",
            description = "description"
        )
        val categories = listOf(category)

        whenever(categoryDao.getCategories()).thenReturn(Single.just(emptyList()))
        whenever(mealDBService.getCategories()).thenReturn(Single.just(response))
        whenever(mapper.map(categoryResponse)).thenReturn(category)
        whenever(categoryDao.insertOrUpdate(categories)).thenReturn(Completable.complete())

        categoryRepo.getCategories()
            .test()
            .assertValue(listOf(category))
            .assertNoErrors()
            .assertComplete()
            .dispose()
    }

    @Test
    fun `when getCategories invoked and dao return list and should not api service`() {
        val category = Category(
            id = "id",
            name = "name",
            thumbnail = "thumbnail",
            description = "description"
        )
        val categories = listOf(category)

        whenever(categoryDao.getCategories()).thenReturn(Single.just(categories))

        categoryRepo.getCategories()
            .test()
            .assertValue(categories)
            .assertNoErrors()
            .assertComplete()
            .dispose()

        verify(mealDBService, never()).getCategories()
        verify(categoryDao, never()).insertOrUpdate(categories)
    }
}
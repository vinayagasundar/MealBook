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
import org.mockito.kotlin.any
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
    fun `when getCategories invoked if service return list then save it local and return list`() {
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

        whenever(mealDBService.getCategories()).thenReturn(Single.just(response))
        whenever(mapper.map(categoryResponse)).thenReturn(category)
        whenever(categoryDao.insertOrUpdate(categories)).thenReturn(Completable.complete())

        categoryRepo.getCategories()
            .test()
            .assertValue(listOf(category))
            .assertNoErrors()
            .assertComplete()
            .dispose()
        verify(categoryDao, never()).getCategories()
    }

    @Test
    fun `when getCategories invoked and service throws error check the local database for result`() {
        val category = Category(
            id = "id",
            name = "name",
            thumbnail = "thumbnail",
            description = "description"
        )
        val categories = listOf(category)

        whenever(mealDBService.getCategories()).thenReturn(Single.error(Exception("Error")))
        whenever(categoryDao.getCategories()).thenReturn(Single.just(categories))

        categoryRepo.getCategories()
            .test()
            .assertValue(categories)
            .assertNoErrors()
            .assertComplete()
            .dispose()

        verify(mapper, never()).map(any())
    }
}
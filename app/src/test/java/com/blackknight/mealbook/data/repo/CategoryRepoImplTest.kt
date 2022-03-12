package com.blackknight.mealbook.data.repo

import com.blackknight.mealbook.data.daos.CategoryDao
import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.mapper.Mapper
import com.blackknight.mealbook.network.MealDBService
import com.blackknight.mealbook.network.response.CategoryListResponse
import com.blackknight.mealbook.network.response.CategoryResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doSuspendableAnswer
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
        runBlocking {
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

            whenever(mealDBService.getCategories()).thenReturn(response)
            whenever(mapper.map(categoryResponse)).thenReturn(category)

            val result = categoryRepo.getCategories()

            Assert.assertEquals(categories, result)

            verify(categoryDao, never()).getCategories()
            verify(categoryDao).insertOrUpdate(categories)
        }
    }

    @Test
    fun `when getCategories invoked and service throws error check the local database for result`() {
        runBlocking {
            val category = Category(
                id = "id",
                name = "name",
                thumbnail = "thumbnail",
                description = "description"
            )
            val categories = listOf(category)

            whenever(mealDBService.getCategories()).doSuspendableAnswer {
                throw Exception("Error")
            }
            whenever(categoryDao.getCategories()).thenReturn(categories)

            val result = categoryRepo.getCategories()

            Assert.assertEquals(categories, result)

            verify(mapper, never()).map(any())
        }
    }
}
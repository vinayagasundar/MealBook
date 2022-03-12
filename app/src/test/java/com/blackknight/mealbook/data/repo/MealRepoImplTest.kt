package com.blackknight.mealbook.data.repo

import com.blackknight.mealbook.data.daos.MealDao
import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.entities.Meal
import com.blackknight.mealbook.data.mapper.Mapper
import com.blackknight.mealbook.network.MealDBService
import com.blackknight.mealbook.network.response.MealListResponse
import com.blackknight.mealbook.network.response.MealResponse
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

class MealRepoImplTest {
    @Mock
    private lateinit var mealDao: MealDao

    @Mock
    private lateinit var mealDBService: MealDBService

    @Mock
    private lateinit var mapper: Mapper<MealResponse, Meal>

    private lateinit var mealRepo: MealRepoImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mealRepo = MealRepoImpl(mealDao, mealDBService, mapper)
    }

    @Test
    fun `when getMealList invoked if service return list then save it local and return list`() {
        runBlocking {
            val mealResponse = MealResponse(
                id = "id",
                name = "name",
                thumbnail = "thumbnail"
            )
            val response = MealListResponse(list = listOf(mealResponse))
            val meal = Meal(
                id = "id",
                name = "name",
                thumbnail = "thumbnail",
                categoryId = "categoryId"
            )
            val mealList = listOf(meal)

            whenever(mealDBService.getMealByCategory("name")).thenReturn(response)
            whenever(mapper.map(mealResponse)).thenReturn(meal)

            val category = Category(
                id = "categoryId",
                name = "name",
                thumbnail = "thumbnail",
                description = "description"
            )
            val result = mealRepo.getMealList(category)

            Assert.assertEquals(mealList, result)

            verify(mealDao, never()).getMealList("categoryId")
            verify(mealDao).insertOrUpdate(mealList)
        }
    }

    @Test
    fun `when getMealList invoked and service throws error check the local database for result`() {
        runBlocking {
            val meal = Meal(
                id = "id",
                name = "name",
                thumbnail = "thumbnail",
                categoryId = "categoryId"
            )
            val mealList = listOf(meal)
            whenever(mealDBService.getMealByCategory("name")).doSuspendableAnswer {
                throw Exception("Error")
            }
            whenever(mealDao.getMealList("categoryId")).thenReturn(mealList)

            val category = Category(
                id = "categoryId",
                name = "name",
                thumbnail = "thumbnail",
                description = "description"
            )
            val result = mealRepo.getMealList(category)

            Assert.assertEquals(mealList, result)
            verify(mapper, never()).map(any())
        }
    }
}
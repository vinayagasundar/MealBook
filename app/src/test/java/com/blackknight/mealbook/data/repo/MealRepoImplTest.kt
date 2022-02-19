package com.blackknight.mealbook.data.repo

import com.blackknight.mealbook.data.daos.MealDao
import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.data.entities.Meal
import com.blackknight.mealbook.data.mapper.Mapper
import com.blackknight.mealbook.network.MealDBService
import com.blackknight.mealbook.network.response.MealListResponse
import com.blackknight.mealbook.network.response.MealResponse
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

        whenever(mealDBService.getMealByCategory("name")).thenReturn(Single.just(response))
        whenever(mapper.map(mealResponse)).thenReturn(meal)
        whenever(mealDao.insertOrUpdate(mealList)).thenReturn(Completable.complete())

        val category = Category(
            id = "categoryId",
            name = "name",
            thumbnail = "thumbnail",
            description = "description"
        )
        mealRepo.getMealList(category)
            .test()
            .assertValue(mealList)
            .assertNoErrors()
            .assertComplete()
            .dispose()

        verify(mealDao, never()).getMealList("categoryId")
    }

    @Test
    fun `when getMealList invoked and service throws error check the local database for result`() {
        val meal = Meal(
            id = "id",
            name = "name",
            thumbnail = "thumbnail",
            categoryId = "categoryId"
        )
        val mealList = listOf(meal)
        whenever(mealDBService.getMealByCategory("name")).thenReturn(Single.error(Exception("Error")))
        whenever(mealDao.getMealList("categoryId")).thenReturn(Single.just(mealList))

        val category = Category(
            id = "categoryId",
            name = "name",
            thumbnail = "thumbnail",
            description = "description"
        )
        mealRepo.getMealList(category)
            .test()
            .assertValue(mealList)
            .assertNoErrors()
            .assertComplete()
            .dispose()

        verify(mapper, never()).map(any())
    }
}
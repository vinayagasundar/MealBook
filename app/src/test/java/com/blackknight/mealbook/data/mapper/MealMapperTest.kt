package com.blackknight.mealbook.data.mapper

import com.blackknight.mealbook.data.entities.Meal
import com.blackknight.mealbook.network.response.MealResponse
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MealMapperTest {

    private lateinit var mapper: MealMapper

    @Before
    fun setUp() {
        mapper = MealMapper()
    }

    @Test
    fun mapper() {
        val expected = Meal(
            id = "id",
            name = "name",
            thumbnail = "thumbnail",
            categoryId = ""
        )
        Assert.assertEquals(
            expected, mapper.map(
                MealResponse(
                    id = "id",
                    name = "name",
                    thumbnail = "thumbnail"
                )
            )
        )
    }
}
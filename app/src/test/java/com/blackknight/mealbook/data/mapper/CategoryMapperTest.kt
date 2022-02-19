package com.blackknight.mealbook.data.mapper

import com.blackknight.mealbook.data.entities.Category
import com.blackknight.mealbook.network.response.CategoryResponse
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CategoryMapperTest {

    private lateinit var mapper: CategoryMapper

    @Before
    fun setUp() {
        mapper = CategoryMapper()
    }

    @Test
    fun mapper() {
        val expected = Category(
            id = "id",
            name = "name",
            thumbnail = "thumbnail",
            description = "description"
        )
        Assert.assertEquals(
            expected, mapper.map(
                CategoryResponse(
                    id = "id",
                    name = "name",
                    thumbnail = "thumbnail",
                    description = "description"
                )
            )
        )
    }
}
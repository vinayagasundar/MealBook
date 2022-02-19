package com.blackknight.mealbook.data.mapper

import com.blackknight.mealbook.data.entities.Ingredient
import com.blackknight.mealbook.data.entities.Recipe
import com.blackknight.mealbook.network.response.RecipeResponse
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RecipeMapperTest {

    private lateinit var mapper: RecipeMapper

    @Before
    fun setUp() {
        mapper = RecipeMapper()
    }

    @Test
    fun map() {
        val response = RecipeResponse(
            id = "id",
            name = "name",
            categories = "category",
            instructions = "instruction",
            thumbnail = "thumbnail",
            strIngredient1 = "name1",
            strMeasure1 = "measure1",
            strIngredient2 = "name2",
            strMeasure2 = "measure2",
        )
        val expected = Recipe(
            id = "id",
            name = "name",
            category = "category",
            instruction = "instruction",
            thumbnail = "thumbnail",
            youtube = "",
            ingredient = listOf(
                Ingredient("name1", "measure1"),
                Ingredient("name2", "measure2")
            )
        )
    }

    @Test
    fun `when invoke getIngredient with ingredient and measure return Ingredient`() {
        val expected = Ingredient("ingredientName", "ingredientMeasure")
        Assert.assertEquals(expected, mapper.getIngredient("ingredientName", "ingredientMeasure"))
    }

    @Test
    fun `when invoke getIngredient with ingredient and measure is null or empty return null`() {
        Assert.assertNull(mapper.getIngredient("ingredientName", null))
        Assert.assertNull(mapper.getIngredient("ingredientName", ""))
    }

    @Test
    fun `when invoke getIngredient with ingredient is null or empty and measure return null`() {
        Assert.assertNull(mapper.getIngredient(null, "ingredientMeasure"))
        Assert.assertNull(mapper.getIngredient("", "ingredientMeasure"))
    }

    @Test
    fun `when invoke toIngredients with valid ingredient return not empty list`() {
        val expected = listOf(
            Ingredient("name1", "measure1"),
            Ingredient("name2", "measure2"),
            Ingredient("name3", "measure3")
        )
        val response = RecipeResponse(
            id = "id",
            name = "name",
            categories = "category",
            instructions = "instruction",
            thumbnail = "thumbnail",
            strIngredient1 = "name1",
            strMeasure1 = "measure1",
            strIngredient2 = "name2",
            strMeasure2 = "measure2",
            strIngredient3 = "name3",
            strMeasure3 = "measure3",
        )
        Assert.assertEquals(expected, mapper.toIngredients(response))
    }

    @Test
    fun `when invoke toIngredients with invalid ingredient return empty list`() {
        val response = RecipeResponse(
            id = "id",
            name = "name",
            categories = "category",
            instructions = "instruction",
            thumbnail = "thumbnail",
        )
        Assert.assertTrue(mapper.toIngredients(response).isEmpty())
    }
}
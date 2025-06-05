package com.yorizori.yoremo.domain.recipes.service

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.CreateRecipes
import com.yorizori.yoremo.domain.categories.port.CategoriesRepository
import com.yorizori.yoremo.domain.foods.entity.Foods
import com.yorizori.yoremo.domain.foods.port.FoodsRepository
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateRecipesService(
    private val recipesRepository: RecipesRepository,
    private val categoriesRepository: CategoriesRepository,
    private val foodsRepository: FoodsRepository
) {
    @Transactional
    fun create(request: CreateRecipes.Request): CreateRecipes.Response {
        val categories = categoriesRepository.findByIdIn(
            listOfNotNull(
                request.categoryTypeId,
                request.categorySituationId,
                request.categoryIngredientId,
                request.categoryMethodId
            )
        )

        val categoriesMap = categories.associateBy { it.categoryId }

        val recipes = Recipes(
            title = request.title,
            description = request.description,
            ingredients = request.ingredients,
            seasonings = request.seasonings,
            instructions = request.instructions,
            categoryType = categoriesMap[request.categoryTypeId],
            categorySituation = categoriesMap[request.categorySituationId],
            categoryIngredient = categoriesMap[request.categoryIngredientId],
            categoryMethod = categoriesMap[request.categoryMethodId],
            prepTime = request.prepTime,
            cookTime = request.cookTime,
            servingSize = request.servingSize,
            difficulty = request.difficulty,
            imageUrl = request.imageUrl,
            tags = request.tags
        )

        val savedRecipes = recipesRepository.save(recipes)

        val foods = Foods(
            name = request.title,
            foodType = Foods.FoodType.RECIPE,
            recipe = savedRecipes,
            caloriesPer100g = request.caloriesPer100g,
        )

        foodsRepository.save(foods)

        return CreateRecipes.Response(
            recipeId = savedRecipes.recipeId!!
        )
    }
}

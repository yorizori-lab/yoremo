package com.yorizori.yoremo.adapter.out.persistence.recipes

import com.yorizori.yoremo.domain.recipes.entity.Recipes
import org.springframework.data.jpa.repository.JpaRepository

interface RecipesJpaRepository : JpaRepository<Recipes, Long>

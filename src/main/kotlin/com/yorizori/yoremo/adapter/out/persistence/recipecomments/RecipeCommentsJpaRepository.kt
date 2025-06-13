package com.yorizori.yoremo.adapter.out.persistence.recipecomments

import com.yorizori.yoremo.domain.recipecomments.entity.RecipeComments
import org.springframework.data.jpa.repository.JpaRepository

interface RecipeCommentsJpaRepository : JpaRepository<RecipeComments, Long>

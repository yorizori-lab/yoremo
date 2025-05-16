package com.yorizori.yoremo.adapter.out.persistence.categories

import com.yorizori.yoremo.domain.categories.entity.Categories
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoriesJpaRepository : JpaRepository<Categories, Long> {
    fun findAllByCategoryType(categoryType: Categories.Type): List<Categories>
}

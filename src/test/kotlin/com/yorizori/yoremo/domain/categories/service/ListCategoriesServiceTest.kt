package com.yorizori.yoremo.domain.categories.service

import com.yorizori.yoremo.adapter.`in`.web.categories.message.ListCategories
import com.yorizori.yoremo.config.FixtureMonkeyUtils.giveMeKotlinBuilder
import com.yorizori.yoremo.config.FixtureMonkeyUtils.giveMeOne
import com.yorizori.yoremo.domain.categories.entity.Categories
import com.yorizori.yoremo.domain.categories.port.CategoriesRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ListCategoriesServiceTest {

    private val categoriesRepository: CategoriesRepository = mockk()

    private val sut = ListCategoriesService(categoriesRepository)

    @Test
    fun listByType() {
        // given
        val request = giveMeOne<ListCategories.Request>()

        every {
            categoriesRepository.findAllByCategoryType(request.categoryType)
        } returns giveMeKotlinBuilder<Categories>()
            .setNotNull(Categories::categoryId)
            .set(Categories::categoryType.name, request.categoryType)
            .sampleList(2)

        // when
        val actual = sut.listByType(request)

        // then
        assertThat(actual.categories)
            .isNotNull
            .hasSize(2)
            .allMatch { it.categoryType == request.categoryType }
    }
}

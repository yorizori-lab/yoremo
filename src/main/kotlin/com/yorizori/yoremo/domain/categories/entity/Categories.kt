package com.yorizori.yoremo.domain.categories.entity

import com.yorizori.yoremo.domain.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate

@Entity
@DynamicUpdate
@Table(name = "categories")
class Categories(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val categoryId: Long? = null,

    val name: String,

    @Enumerated(EnumType.STRING)
    val categoryType: Type,

    val description: String? = null
) : BaseEntity() {

    enum class Type(val description: String) {
        TYPE("종류별"),
        SITUATION("상황별"),
        INGREDIENT("재료별"),
        METHOD("방법별")
    }
}

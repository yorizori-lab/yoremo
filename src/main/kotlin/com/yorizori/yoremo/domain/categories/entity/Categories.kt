package com.yorizori.yoremo.domain.categories.entity

import jakarta.persistence.*

@Entity
@Table(name = "categories")
class Categories(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val categoryId: Long? = null,

    val name: String? = null,

    val categoryType: String? = null,

    val description: String? = null
)
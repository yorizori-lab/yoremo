package com.yorizori.yoremo.domain.categories.entity

import jakarta.persistence.*

@Entity
@Table(name = "categories")
class Categories(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val categoryId: Int? = null
)
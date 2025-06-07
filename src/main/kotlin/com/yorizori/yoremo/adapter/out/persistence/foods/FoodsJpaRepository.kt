package com.yorizori.yoremo.adapter.out.persistence.foods

import com.yorizori.yoremo.domain.foods.entity.Foods
import org.springframework.data.jpa.repository.JpaRepository

interface FoodsJpaRepository : JpaRepository<Foods, Long>

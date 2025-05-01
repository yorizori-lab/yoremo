package com.yorizori.yoremo.adapter.out.persistence.sample

import com.yorizori.yoremo.domain.sample.entity.Sample
import org.springframework.data.jpa.repository.JpaRepository

interface SampleJpaRepository : JpaRepository<Sample, Long>

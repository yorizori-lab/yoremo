package com.yorizori.yoremo.adapter.postgresql.sample

import org.springframework.data.jpa.repository.JpaRepository

interface SampleRepository : JpaRepository<SampleEntity, Long>

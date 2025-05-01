package com.yorizori.yoremo.adapter.postgresql.sample

import com.yorizori.yoremo.adapter.postgresql.common.BaseEntity
import com.yorizori.yoremo.model.Sample
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "sample")
class SampleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val message: String,
) : BaseEntity() {

    companion object {
        fun from(sample: Sample): SampleEntity {
            return SampleEntity(id = sample.id, message = sample.message)
        }
    }

    fun toModel(): Sample {
        return Sample(
            id = id,
            message = message,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}

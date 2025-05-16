package com.yorizori.yoremo.domain.common.convert

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class JsonStringConverter : AttributeConverter<String, String> {
    override fun convertToDatabaseColumn(attribute: String?): String? {
        return attribute
    }

    override fun convertToEntityAttribute(dbData: String?): String? {
        return dbData
    }
}
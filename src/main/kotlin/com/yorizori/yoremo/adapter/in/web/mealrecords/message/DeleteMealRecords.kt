package com.yorizori.yoremo.adapter.`in`.web.mealrecords.message

abstract class DeleteMealRecords {
    data class Request(
        val recordIds: List<Long>
    )

    data class Response(
        val success: Boolean
    )
}

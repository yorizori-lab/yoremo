package com.yorizori.yoremo.domain.foods.service

import com.yorizori.yoremo.adapter.`in`.web.foods.message.Calorie
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatModel
import org.springframework.stereotype.Service

@Service
class CalorieCalculatorService(
    chatModel: ChatModel
) {

    private val chatClent = ChatClient.builder(chatModel)
        .defaultSystem("""
           당신은 칼로리 계산 전문가입니다.
           사용자가 제공하는 요리 제목과 재료를 바탕으로 총 칼로리를 계산해주세요.
           응답은 반드시 숫자만 반환해주세요. (예: 350)
           단위나 다른 설명 없이 칼로리 숫자만 답변하세요.
       """.trimIndent())
        .build()

    fun caculateCalories(request: Calorie.Request): Calorie.Response {
        val prompt = """
            요리명: ${request.title}
            재료: ${request.ingredients.joinToString(", ")}
            
            총 칼로리를 숫자만 알려주세요.
        """.trimIndent()

        val response = chatClent.prompt()
            .user(prompt)
            .call()
            .content()

        return Calorie.Response(
            caloriesPer100g = response?.toInt() ?: 0
        )
    }

}

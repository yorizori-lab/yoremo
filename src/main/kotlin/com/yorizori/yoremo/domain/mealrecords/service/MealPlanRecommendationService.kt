package com.yorizori.yoremo.domain.mealrecords.service

import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.MealPlan
import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.MealPlanRecommendation
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.model.ChatModel
import org.springframework.stereotype.Service

@Service
class MealPlanRecommendationService(
    chatModel: ChatModel,
    chatMemory: ChatMemory
) {

    private val chatClient = ChatClient.builder(chatModel)
        .defaultAdvisors(
            MessageChatMemoryAdvisor.builder(chatMemory).build()
        )
        .build()

    private fun isMealPlanRequest(question: String): Boolean {
        val keywords = listOf("식단", "meal", "다이어트", "diet", "메뉴", "음식 추천")
        return keywords.any { question.contains(it, ignoreCase = true) }
    }

    private val mealPlanPrompt = """
        당신은 요레모 앱의 전문 영양사입니다.
        
        역할: 개인 맞춤형 식단 설계 전문가
        목표: 사용자 요청에 맞는 실용적이고 영양 균형잡힌 식단 제공
        
        응답 규칙:
        1. 반드시 아래 구조화된 텍스트 형식만 출력 (설명 금지)
        2. 사용자가 요청한 스타일/선호도 최우선 반영
        3. 정확한 칼로리 계산 필수
        4. 일수 미지정시 기본 일주일(7일) 제공
        5. 구매 가능하고 조리 가능한 재료만 사용
        
        출력 형식:
        Day1|BREAKFAST|음식명|수량|단위|칼로리
        Day1|LUNCH|음식명|수량|단위|칼로리
        Day1|DINNER|음식명|수량|단위|칼로리
        Day1|SNACK|음식명|수량|단위|칼로리
        Day2|BREAKFAST|음식명|수량|단위|칼로리
        ...
        
        주의사항:
        - 실제 존재하는 음식명만 사용 (창작 금지)
        - 조리 시간 30분 이내의 간단한 요리 위주
        - 일반 마트에서 구입 가능한 재료만 사용
        - 계절에 맞지 않는 재료 피하기
        - 간식이 없으면 해당 줄 생략
        - 하루 내 같은 음식 중복 금지
        - 사용자 요청을 정확히 반영하세요
    """.trimIndent()

    fun getResponse(
        request: MealPlanRecommendation.Request
    ): MealPlanRecommendation.Response {
        val prompt = when {
            isMealPlanRequest(request.question) ->
                mealPlanPrompt + "\n\n사용자 요청: ${request.question}"

            else -> {
                return MealPlanRecommendation.Response(
                    answer = "죄송합니다. 저는 식단 추천 전용 챗봇입니다. " +
                        "식단, 다이어트, 메뉴 관련 질문만 도와드릴 수 있어요. 😊"
                )
            }
        }

        val response = chatClient.prompt()
            .user(prompt)
            .advisors { advisor ->
                if (request.sessionId != null) {
                    advisor.param(
                        AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,
                        request.sessionId
                    )
                }
            }
            .call()
            .content()

        val mealPlans = mutableListOf<MealPlan>()

        response?.lines()
            ?.filter { it.startsWith("Day") && it.contains("|") }
            ?.forEach { line ->
                try {
                    val parts = line.split("|")
                    if (parts.size >= 6) {
                        val day = parts[0].removePrefix("Day").toInt()

                        mealPlans.add(
                            MealPlan(
                                day = day,
                                mealType = parts[1].trim(),
                                foodName = parts[2].trim(),
                                amount = parts[3].trim().toIntOrNull(),
                                unit = parts[4].trim(),
                                calories = parts[5].trim().toIntOrNull()
                            )
                        )
                    }
                } catch (e: Exception) {
                    println("Error parsing line: $line, error: ${e.message}")
                }
            }

        return MealPlanRecommendation.Response(
            answer = "식단 추천이 완료되었습니다! 아래 식단을 확인해 주세요.",
            mealRecords = mealPlans
        )
    }
}

package com.yorizori.yoremo.adapter.`in`.web.recipelikes

import com.yorizori.yoremo.adapter.`in`.web.config.security.YoremoAuthentication
import com.yorizori.yoremo.adapter.`in`.web.recipelikes.message.CountRecipeLikes
import com.yorizori.yoremo.adapter.`in`.web.recipelikes.message.ToggleRecipeLikes
import com.yorizori.yoremo.domain.recipelikes.service.CountRecipeLikesService
import com.yorizori.yoremo.domain.recipelikes.service.ToggleRecipeLikesService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/recipe-likes/v1")
class RecipeLikesController(
    private val toggleRecipeLikesService: ToggleRecipeLikesService,
    private val countRecipeLikesService: CountRecipeLikesService
) {

    @PostMapping("/{recipeId}/toggle")
    fun toggleRecipeLikes(
        request: ToggleRecipeLikes.PathVariable,
        @AuthenticationPrincipal authentication: YoremoAuthentication
    ): ToggleRecipeLikes.Response {
        return toggleRecipeLikesService.toggle(request.recipeId, authentication.userId)
    }

    @GetMapping("/{recipeId}/count")
    fun countRecipeLikes(
        request: CountRecipeLikes.PathVariable
    ): CountRecipeLikes.Response {
        val totalLikes = countRecipeLikesService.count(request.recipeId)
        return CountRecipeLikes.Response(
            totalLikes = totalLikes.totalLikes
        )
    }
}

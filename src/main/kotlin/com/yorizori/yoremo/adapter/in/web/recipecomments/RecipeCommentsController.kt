package com.yorizori.yoremo.adapter.`in`.web.recipecomments

import com.yorizori.yoremo.adapter.`in`.web.config.security.YoremoAuthentication
import com.yorizori.yoremo.adapter.`in`.web.recipecomments.message.CreateRecipeComments
import com.yorizori.yoremo.adapter.`in`.web.recipecomments.message.DeleteRecipeComments
import com.yorizori.yoremo.adapter.`in`.web.recipecomments.message.ListRecipeComments
import com.yorizori.yoremo.adapter.`in`.web.recipecomments.message.UpdateRecipeComments
import com.yorizori.yoremo.domain.recipecomments.service.CreateRecipeCommentsService
import com.yorizori.yoremo.domain.recipecomments.service.DeleteRecipeCommentsService
import com.yorizori.yoremo.domain.recipecomments.service.ListRecipeCommentsService
import com.yorizori.yoremo.domain.recipecomments.service.UpdateRecipeCommentsService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/recipe-comments/v1")
class RecipeCommentsController(
    private val createRecipeCommentsService: CreateRecipeCommentsService,
    private val updateRecipeCommentsService: UpdateRecipeCommentsService,
    private val deleteRecipeCommentsService: DeleteRecipeCommentsService,
    private val listRecipeCommentsService: ListRecipeCommentsService
) {

    @PostMapping("/recipes/{recipeId}/comments")
    fun create(
        request: CreateRecipeComments.PathVariable,
        @RequestBody createRequest: CreateRecipeComments.Request,
        @AuthenticationPrincipal authentication: YoremoAuthentication
    ): CreateRecipeComments.Response {
        return createRecipeCommentsService.create(
            request.recipeId,
            createRequest,
            authentication.userId
        )
    }

    @GetMapping("/recipes/{recipeId}/comments")
    fun list(
        pathVariable: ListRecipeComments.PathVariable,
        request: ListRecipeComments.Request
    ): ListRecipeComments.Response {
        return listRecipeCommentsService.list(pathVariable.recipeId, request)
    }

    @PutMapping("/comments/{commentId}")
    fun update(
        request: UpdateRecipeComments.PathVariable,
        @RequestBody updateRequest: UpdateRecipeComments.Request,
        @AuthenticationPrincipal authentication: YoremoAuthentication
    ): UpdateRecipeComments.Response {
        return updateRecipeCommentsService.update(
            request.commentId,
            updateRequest,
            authentication.userId
        )
    }

    @DeleteMapping("/comments/{commentId}")
    fun delete(
        request: DeleteRecipeComments.PathVariable,
        @AuthenticationPrincipal authentication: YoremoAuthentication
    ): DeleteRecipeComments.Response {
        return deleteRecipeCommentsService.delete(
            request.commentId,
            authentication.userId
        )
    }
}

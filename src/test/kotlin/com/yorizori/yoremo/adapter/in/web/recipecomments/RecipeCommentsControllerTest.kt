package com.yorizori.yoremo.adapter.`in`.web.recipecomments

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.yorizori.yoremo.adapter.`in`.web.recipecomments.message.CreateRecipeComments
import com.yorizori.yoremo.adapter.`in`.web.recipecomments.message.DeleteRecipeComments
import com.yorizori.yoremo.adapter.`in`.web.recipecomments.message.ListRecipeComments
import com.yorizori.yoremo.adapter.`in`.web.recipecomments.message.UpdateRecipeComments
import com.yorizori.yoremo.domain.recipecomments.service.CreateRecipeCommentsService
import com.yorizori.yoremo.domain.recipecomments.service.DeleteRecipeCommentsService
import com.yorizori.yoremo.domain.recipecomments.service.ListRecipeCommentsService
import com.yorizori.yoremo.domain.recipecomments.service.UpdateRecipeCommentsService
import com.yorizori.yoremo.test.RestDocsSupport
import com.yorizori.yoremo.test.YoremoControllerTest
import com.yorizori.yoremo.test.security.WithYoremoUser
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant

@WithYoremoUser
@YoremoControllerTest
@WebMvcTest(RecipeCommentsController::class)
class RecipeCommentsControllerTest : RestDocsSupport() {

    @MockkBean
    private lateinit var createRecipeCommentsService: CreateRecipeCommentsService

    @MockkBean
    private lateinit var updateRecipeCommentsService: UpdateRecipeCommentsService

    @MockkBean
    private lateinit var deleteRecipeCommentsService: DeleteRecipeCommentsService

    @MockkBean
    private lateinit var listRecipeCommentsService: ListRecipeCommentsService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun initController(): Any {
        return RecipeCommentsController(
            createRecipeCommentsService,
            updateRecipeCommentsService,
            deleteRecipeCommentsService,
            listRecipeCommentsService
        )
    }

    @Test
    fun listRecipeComments() {
        // given
        val recipeId = 1L
        val request = ListRecipeComments.Request(
            page = 0,
            pageSize = 10
        )

        every {
            listRecipeCommentsService.list(any(), any())
        } returns ListRecipeComments.Response(
            totalCount = 2,
            comments = listOf(
                ListRecipeComments.ResponseItem(
                    commentId = 1L,
                    content = "정말 맛있어 보이는 레시피네요!",
                    userId = 1L,
                    userName = "김요리",
                    parentCommentId = null,
                    depth = 0,
                    isDeleted = false,
                    childComments = listOf(
                        ListRecipeComments.ResponseItem(
                            commentId = 2L,
                            content = "저도 해봤는데 정말 맛있었어요!",
                            userId = 2L,
                            userName = "박요리",
                            parentCommentId = 1L,
                            depth = 1,
                            isDeleted = false,
                            childComments = emptyList(),
                            createdAt = Instant.now(),
                            updatedAt = Instant.now()
                        )
                    ),
                    createdAt = Instant.now(),
                    updatedAt = Instant.now()
                )
            )
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/recipe-comments/v1/recipes/{recipeId}/comments", recipeId)
                .param("page", request.page.toString())
                .param("pageSize", request.pageSize.toString())
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.total_count").value(2))
            .andExpect(jsonPath("$.comments[0].comment_id").value(1L))
            .andExpect(jsonPath("$.comments[0].content").value("정말 맛있어 보이는 레시피네요!"))
            .andDo(
                document(
                    "recipe-comment/list",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("recipeId").description("댓글을 조회할 레시피 ID")
                    ),
                    responseFields(
                        fieldWithPath("total_count").description("총 댓글 수"),
                        fieldWithPath("comments[]").description("댓글 목록"),
                        fieldWithPath("comments[].comment_id").description("댓글 ID"),
                        fieldWithPath("comments[].content").description("댓글 내용"),
                        fieldWithPath("comments[].user_id").description("작성자 ID"),
                        fieldWithPath("comments[].user_name").description("작성자 이름"),
                        fieldWithPath("comments[].parent_comment_id").optional().description(
                            "부모 댓글 ID"
                        ),
                        fieldWithPath("comments[].depth").description("댓글 깊이"),
                        fieldWithPath("comments[].is_deleted").description("삭제 여부"),
                        fieldWithPath("comments[].child_comments[]").description("대댓글 목록"),
                        fieldWithPath("comments[].child_comments[].comment_id").description(
                            "대댓글 ID"
                        ),
                        fieldWithPath("comments[].child_comments[].content").description("대댓글 내용"),
                        fieldWithPath("comments[].child_comments[].user_id").description(
                            "대댓글 작성자 ID"
                        ),
                        fieldWithPath("comments[].child_comments[].user_name").description(
                            "대댓글 작성자 이름"
                        ),
                        fieldWithPath("comments[].child_comments[].parent_comment_id").description(
                            "부모 댓글 ID"
                        ),
                        fieldWithPath("comments[].child_comments[].depth").description("대댓글 깊이"),
                        fieldWithPath("comments[].child_comments[].is_deleted").description(
                            "대댓글 삭제 여부"
                        ),
                        fieldWithPath("comments[].child_comments[].child_comments[]").description(
                            "대댓글의 자식 댓글들"
                        ),
                        fieldWithPath("comments[].child_comments[].created_at").description(
                            "대댓글 생성 시간"
                        ),
                        fieldWithPath("comments[].child_comments[].updated_at").description(
                            "대댓글 수정 시간"
                        ),
                        fieldWithPath("comments[].created_at").description("댓글 생성 시간"),
                        fieldWithPath("comments[].updated_at").description("댓글 수정 시간")
                    )
                )
            )
    }

    @Test
    fun createRecipeComment() {
        // given
        val recipeId = 1L
        val request = CreateRecipeComments.Request(
            content = "This is a comment.",
            parentCommentId = null
        )

        every {
            createRecipeCommentsService.create(any(), any(), any())
        } returns CreateRecipeComments.Response(
            commentId = 1L
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/recipe-comments/v1/recipes/{recipeId}/comments", recipeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.comment_id").value(1L))
            .andDo(
                document(
                    "recipe-comment/create",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("recipeId").description("댓글을 작성할 레시피 ID")
                    ),
                    responseFields(
                        fieldWithPath("comment_id").description("생성된 댓글 ID")
                    )
                )
            )
    }

    @Test
    fun updateRecipeComment() {
        // given
        val commentId = 1L
        val request = UpdateRecipeComments.Request(
            content = "Updated comment content."
        )

        every {
            updateRecipeCommentsService.update(any(), any(), any())
        } returns UpdateRecipeComments.Response(
            commentId = commentId
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .put("/api/recipe-comments/v1/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.comment_id").value(commentId))
            .andDo(
                document(
                    "recipe-comment/update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("commentId").description("수정할 댓글 ID")
                    ),
                    responseFields(
                        fieldWithPath("comment_id").description("수정된 댓글 ID")
                    )
                )
            )
    }

    @Test
    fun deleteRecipeComment() {
        // given
        val commentId = 1L

        every {
            deleteRecipeCommentsService.delete(any(), any())
        } returns DeleteRecipeComments.Response(
            commentId = commentId
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/api/recipe-comments/v1/comments/{commentId}", commentId)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.comment_id").value(commentId))
            .andDo(
                document(
                    "recipe-comment/delete",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("commentId").description("삭제할 댓글 ID")
                    ),
                    responseFields(
                        fieldWithPath("comment_id").description("삭제된 댓글 ID")
                    )
                )
            )
    }
}

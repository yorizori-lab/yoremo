package com.yorizori.yoremo.adapter.`in`.web.users

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.yorizori.yoremo.adapter.`in`.web.users.message.Register
import com.yorizori.yoremo.adapter.`in`.web.users.message.SendVerification
import com.yorizori.yoremo.adapter.`in`.web.users.message.VerifyEmail
import com.yorizori.yoremo.domain.users.service.EmailVerificationService
import com.yorizori.yoremo.domain.users.service.RegisterService
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
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WithYoremoUser
@YoremoControllerTest
@WebMvcTest(UsersController::class)
class UsersControllerTest : RestDocsSupport() {

    @MockkBean
    private lateinit var emailVerificationService: EmailVerificationService

    @MockkBean
    private lateinit var registerService: RegisterService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun initController(): Any {
        return UsersController(
            emailVerificationService,
            registerService
        )
    }

    @Test
    fun sendVerification() {
        // given
        val request = SendVerification.Request(
            email = "test@test.com"
        )

        every {
            emailVerificationService.sendVerificationCode(any())
        } returns SendVerification.Response(
            success = true
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/users/v1/send-verification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andDo(
                document(
                    "users/send-verification",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("email").description("이메일 주소")
                    ),
                    responseFields(
                        fieldWithPath("success").description("인증 코드 전송 성공 여부")
                    )
                )
            )
    }

    @Test
    fun verifyEmail() {
        // given
        val request = VerifyEmail.Request(
            email = "test@test.com",
            code = "123456"
        )

        every {
            emailVerificationService.verifyCode(any())
        } returns VerifyEmail.Response(
            success = true
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/users/v1/verify-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andDo(
                document(
                    "users/verify-email",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("email").description("인증할 이메일 주소"),
                        fieldWithPath("code").description("이메일로 받은 6자리 인증 코드")
                    ),
                    responseFields(
                        fieldWithPath("success").description("이메일 인증 성공 여부")
                    )
                )
            )
    }

    @Test
    fun register() {
        // given
        val request = Register.Request(
            email = "test@test.com",
            password = "123456",
            name = "testName",
            profileImageUrl = "https://example.com/profile.jpg"
        )

        every {
            registerService.register(any())
        } returns Register.Response(
            email = "test@test.com"
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/users/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email").value("test@test.com"))
            .andDo(
                document(
                    "users/register",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("email").description("사용자 이메일 주소 (인증 완료된 이메일)"),
                        fieldWithPath("password").optional().description(
                            "비밀번호 (OAuth 가입 시 null 가능)"
                        ),
                        fieldWithPath("name").description("사용자 이름"),
                        fieldWithPath("profile_image_url").optional().description("프로필 이미지 URL")
                    ),
                    responseFields(
                        fieldWithPath("email").description("가입 완료된 사용자 이메일")
                    )
                )
            )
    }

    @Test
    fun getCurrentUser() {
        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/users/v1/me")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.user_id").value(1L))
            .andExpect(jsonPath("$.email").value("test@test.com"))
            .andDo(
                document(
                    "users/me",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("user_id").description("현재 로그인한 사용자 ID"),
                        fieldWithPath("email").description("현재 로그인한 사용자 이메일")
                    )
                )
            )
    }
}

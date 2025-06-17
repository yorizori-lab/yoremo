package com.yorizori.yoremo.adapter.`in`.web.shoppingcart

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message.CreateShoppingCart
import com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message.DeleteShoppingCart
import com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message.GetShoppingCart
import com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message.UpdateShoppingCart
import com.yorizori.yoremo.domain.shoppingcart.service.CreateShoppingCartService
import com.yorizori.yoremo.domain.shoppingcart.service.DeleteShoppingCartService
import com.yorizori.yoremo.domain.shoppingcart.service.GetShoppingCartService
import com.yorizori.yoremo.domain.shoppingcart.service.UpdateShoppingCartService
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
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.Instant

@WithYoremoUser
@YoremoControllerTest
@WebMvcTest(ShoppingCartController::class)
class ShoppingCartControllerTest : RestDocsSupport() {

    @MockkBean
    private lateinit var createShoppingCartService: CreateShoppingCartService

    @MockkBean
    private lateinit var updateShoppingCartService: UpdateShoppingCartService

    @MockkBean
    private lateinit var deleteShoppingCartService: DeleteShoppingCartService

    @MockkBean
    private lateinit var getShoppingCartService: GetShoppingCartService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun initController(): Any {
        return ShoppingCartController(
            createShoppingCartService,
            updateShoppingCartService,
            deleteShoppingCartService,
            getShoppingCartService
        )
    }

    @Test
    fun createShoppingCart() {
        // given
        val request = CreateShoppingCart.Request(
            shoppingCarts = listOf(
                CreateShoppingCart.ShoppingCartItemRequest(
                    foodName = "당근",
                    totalAmount = BigDecimal("500.0"),
                    unit = "g",
                    notes = "유기농으로 구매"
                ),
                CreateShoppingCart.ShoppingCartItemRequest(
                    foodName = "양파",
                    totalAmount = BigDecimal("2.0"),
                    unit = "개"
                )
            )
        )

        every {
            createShoppingCartService.create(any(), any())
        } returns CreateShoppingCart.Response(success = true)

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/shopping-cart/v1/shopping-carts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andDo(
                document(
                    "shopping-cart/create",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("shopping_carts").description("장바구니 아이템 목록"),
                        fieldWithPath("shopping_carts[].food_name").description("음식/재료명"),
                        fieldWithPath("shopping_carts[].total_amount").description("총 필요량"),
                        fieldWithPath("shopping_carts[].unit").description("단위"),
                        fieldWithPath("shopping_carts[].notes").optional().description("메모")
                    ),
                    responseFields(
                        fieldWithPath("success").description("생성 성공 여부")
                    )
                )
            )
    }

    @Test
    fun getShoppingCart() {
        // given
        val request = GetShoppingCart.Request(
            purchased = false
        )

        every {
            getShoppingCartService.get(any(), any())
        } returns GetShoppingCart.Response(
            shoppingCarts = listOf(
                GetShoppingCart.ResponseItem(
                    cartId = 1L,
                    userId = 1L,
                    foodName = "당근",
                    totalAmount = BigDecimal("500.0"),
                    unit = "g",
                    purchased = false,
                    notes = "유기농으로 구매",
                    createdAt = Instant.now(),
                    updatedAt = Instant.now()
                ),
                GetShoppingCart.ResponseItem(
                    cartId = 2L,
                    userId = 1L,
                    foodName = "양파",
                    totalAmount = BigDecimal("2.0"),
                    unit = "개",
                    purchased = false,
                    notes = null,
                    createdAt = Instant.now(),
                    updatedAt = Instant.now()
                )
            )
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/shopping-cart/v1/shopping-carts")
                .param("purchased", request.purchased.toString())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.shopping_carts").isArray)
            .andExpect(jsonPath("$.shopping_carts[0].cart_id").value(1L))
            .andExpect(jsonPath("$.shopping_carts[0].food_name").value("당근"))
            .andExpect(jsonPath("$.shopping_carts[0].purchased").value(false))
            .andDo(
                document(
                    "shopping-cart/get",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("purchased").optional().description("구매 상태 필터")
                    ),
                    responseFields(
                        fieldWithPath("shopping_carts").description("장바구니 아이템 목록"),
                        fieldWithPath("shopping_carts[].cart_id").description("장바구니 아이템 ID"),
                        fieldWithPath("shopping_carts[].user_id").description("사용자 ID"),
                        fieldWithPath("shopping_carts[].food_name").description("음식/재료명"),
                        fieldWithPath("shopping_carts[].total_amount").description("총 필요량"),
                        fieldWithPath("shopping_carts[].unit").description("단위"),
                        fieldWithPath("shopping_carts[].purchased").description("구매 완료 여부"),
                        fieldWithPath("shopping_carts[].notes").optional().description("메모"),
                        fieldWithPath("shopping_carts[].created_at").description("생성 일시"),
                        fieldWithPath("shopping_carts[].updated_at").description("수정 일시")
                    )
                )
            )
    }

    @Test
    fun updateShoppingCart() {
        // given
        val request = UpdateShoppingCart.Request(
            shoppingCarts = listOf(
                UpdateShoppingCart.UpdateShoppingCartItemRequest(
                    cartId = 1L,
                    foodName = "당근",
                    totalAmount = BigDecimal("1000.0"),
                    unit = "g",
                    purchased = false,
                    notes = "유기농으로 구매 - 양 늘림"
                ),
                UpdateShoppingCart.UpdateShoppingCartItemRequest(
                    cartId = 2L,
                    foodName = "양파",
                    totalAmount = BigDecimal("3.0"),
                    unit = "개",
                    purchased = true,
                    notes = "구매 완료"
                )
            )
        )

        every {
            updateShoppingCartService.update(any(), any())
        } returns UpdateShoppingCart.Response(success = true)

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .put("/api/shopping-cart/v1/shopping-carts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andDo(
                document(
                    "shopping-cart/update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("shopping_carts").description("수정할 장바구니 아이템 목록"),
                        fieldWithPath("shopping_carts[].cart_id").description("장바구니 아이템 ID"),
                        fieldWithPath("shopping_carts[].food_name").description("음식/재료명"),
                        fieldWithPath("shopping_carts[].total_amount").description("총 필요량"),
                        fieldWithPath("shopping_carts[].unit").description("단위"),
                        fieldWithPath("shopping_carts[].purchased").description("구매 완료 여부"),
                        fieldWithPath("shopping_carts[].notes").optional().description("메모")
                    ),
                    responseFields(
                        fieldWithPath("success").description("수정 성공 여부")
                    )
                )
            )
    }

    @Test
    fun deleteShoppingCart() {
        // given
        val request = DeleteShoppingCart.Request(
            cartIds = listOf(1L, 2L, 3L)
        )

        every {
            deleteShoppingCartService.delete(any(), any())
        } returns DeleteShoppingCart.Response(success = true)

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/api/shopping-cart/v1/shopping-carts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andDo(
                document(
                    "shopping-cart/delete",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("cart_ids").description("삭제할 장바구니 아이템 ID 목록")
                    ),
                    responseFields(
                        fieldWithPath("success").description("삭제 성공 여부")
                    )
                )
            )
    }
}

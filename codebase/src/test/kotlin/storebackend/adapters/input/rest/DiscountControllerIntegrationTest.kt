package storebackend.adapters.input.rest

import storebackend.adapters.input.rest.dto.CreateDiscountDtoBuilder
import storebackend.adapters.output.repository.InMemoryDiscountRepositoryImpl
import storebackend.domain.models.Discount
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject
import javax.transaction.Transactional

@QuarkusTest
class DiscountControllerIntegrationTest {
    @Inject
    private lateinit var createDiscountDtoBuilder: CreateDiscountDtoBuilder

    @Inject
    private lateinit var repository: InMemoryDiscountRepositoryImpl

    @BeforeEach
    fun setUp() {
        repository.clearAll()
    }

    @Test
    fun `should be able to add a discount`() {
        //given
        givenDiscountRepositoryHas()
        val createDiscountDto = createDiscountDtoBuilder
            .setId("product-a")
            .setProductCount(2)
            .setDiscountInPercentage(50)
            .build()

        //when
        val response: Response = given()
            .contentType(ContentType.JSON)
            .and()
            .body(createDiscountDto)
            .`when`()
            .post("/discount")

        //then
        response
            .then()
            .statusCode(204)

        Assertions.assertEquals(1, repository.listDiscounts().entries.size)

        val discount = repository.getDiscount("product-a")
        Assertions.assertNotNull(discount)
        if (discount != null) {
            Assertions.assertEquals(2, discount.productCount)
            Assertions.assertEquals(50, discount.discountInPercentage)
        }
    }

    @Test
    fun `should be able to modify an existing discount`() {
        //given
        givenDiscountRepositoryHas(SAMPLE_DISCOUNT_A, SAMPLE_DISCOUNT_B)
        val createDiscountADto = createDiscountDtoBuilder
            .setId("product-a")
            .setProductCount(3)
            .setDiscountInPercentage(45)
            .build()

        //when
        val response: Response = given()
            .contentType(ContentType.JSON)
            .and()
            .body(createDiscountADto)
            .`when`()
            .post("/discount")

        //then
        response
            .then()
            .statusCode(204)

        Assertions.assertEquals(2, repository.listDiscounts().entries.size)

        val discount = repository.getDiscount("product-a")
        Assertions.assertNotNull(discount)
        if (discount != null) {
            Assertions.assertEquals(3, discount.productCount)
            Assertions.assertEquals(45, discount.discountInPercentage)
        }
    }

    @Test
    fun `should be able to remove a discount`() {
        //given
        givenDiscountRepositoryHas(SAMPLE_DISCOUNT_A, SAMPLE_DISCOUNT_B)
        val response: Response = given()
            .`when`()
            .delete("/discount/${SAMPLE_DISCOUNT_B.productId}")

        //then
        response
            .then()
            .statusCode(204)

        Assertions.assertEquals(1, repository.listDiscounts().entries.size)
    }

    @Transactional
    private fun givenDiscountRepositoryHas(vararg discounts: Discount) {
        discounts.forEach { discount: Discount -> repository.addDiscount(discount) }
    }

    companion object {
        val SAMPLE_DISCOUNT_A = Discount("product-a", 2, 50, "Buy 1 get 1 free")
        val SAMPLE_DISCOUNT_B = Discount("product-b", 3, 10, "Buy 3 get 10 percent off")
    }
}
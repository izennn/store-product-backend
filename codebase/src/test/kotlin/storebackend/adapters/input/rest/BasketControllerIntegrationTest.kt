package storebackend.adapters.input.rest

import storebackend.adapters.input.rest.dto.CreateDiscountDtoBuilder
import storebackend.adapters.input.rest.dto.UpdateBasketDtoBuilder
import storebackend.adapters.output.repository.InMemoryBasketRepositoryImpl
import storebackend.adapters.output.repository.InMemoryDiscountRepositoryImpl
import storebackend.adapters.output.repository.InMemoryProductRepositoryImpl
import storebackend.domain.Utils
import storebackend.domain.models.Basket
import storebackend.domain.models.BasketBuilder
import storebackend.domain.models.Discount
import storebackend.domain.models.Product
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.*
import javax.inject.Inject
import javax.transaction.Transactional

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BasketControllerIntegrationTest {
    @Inject
    private lateinit var updateBasketDtoBuilder: UpdateBasketDtoBuilder

    @Inject
    private lateinit var basketRepository: InMemoryBasketRepositoryImpl

    @Inject
    private lateinit var productRepository: InMemoryProductRepositoryImpl

    @Inject
    private lateinit var discountRepository: InMemoryDiscountRepositoryImpl

    @BeforeEach
    fun setUp() {
        productRepository.clearAll()
        basketRepository.clearAll()
        discountRepository.clearAll()
    }

    @AfterAll
    fun tearDown() {
        productRepository.clearAll()
        basketRepository.clearAll()
        discountRepository.clearAll()
    }

    @Test
    fun `should be able to modify basket products`() {
        //given
        givenBasket(SAMPLE_BASKET)
        val newBasket = updateBasketDtoBuilder
            .setProductCount(
                mutableMapOf(
                    "product-a" to 6,
                    "product-b" to 1,
                    "product-c" to 7
                )
            ).build()

        //when
        val response: Response = given()
            .contentType(ContentType.JSON)
            .and()
            .body(newBasket)
            .`when`()
            .put("/basket")

        //then
        response
            .then()
            .statusCode(200)

        val actualProductCount = basketRepository.getBasket().productCount
        Assertions.assertEquals(3, actualProductCount.size)
        Assertions.assertEquals(6, actualProductCount["product-a"])
        Assertions.assertEquals(1, actualProductCount["product-b"])
        Assertions.assertEquals(7, actualProductCount["product-c"])
    }

    @Test
    fun `on checkout, a receipt of items along with total price should be returned`() {
        //given
        givenProducts(SAMPLE_PRODUCT_A, SAMPLE_PRODUCT_B)
        givenBasket(SAMPLE_BASKET)
        givenDiscounts(DISCOUNT_A, DISCOUNT_B)

        //when
        val response: Response = given()
            .`when`()
            .get("/basket/checkout")

        //then
        val expectedTotalPriceBeforeDiscount = Utils.roundOffDecimal(
            Utils.roundOffDecimal(SAMPLE_PRODUCT_A.price * 1) +
                    Utils.roundOffDecimal(SAMPLE_PRODUCT_B.price * 5)
        )
        val expectedTotalDiscount = Utils.roundOffDecimal(
            Utils.roundOffDecimal(SAMPLE_PRODUCT_A.price * DISCOUNT_A.discountInPercentage * 0.01)
                    + Utils.roundOffDecimal(SAMPLE_PRODUCT_B.price * DISCOUNT_B.discountInPercentage * 0.01 * 2)
        )
        val expectedTotalPrice = Utils.roundOffDecimal(
            Utils.roundOffDecimal(SAMPLE_PRODUCT_A.price * 1) - Utils.roundOffDecimal(SAMPLE_PRODUCT_A.price * DISCOUNT_A.discountInPercentage * 0.01)
                    + Utils.roundOffDecimal(SAMPLE_PRODUCT_B.price * 5) - Utils.roundOffDecimal(SAMPLE_PRODUCT_B.price * DISCOUNT_B.discountInPercentage * 0.01 * 2)
        )
        response
            .then()
            .statusCode(200)
            .body(
                "items.size()", `is`(2),
                "totalPriceBeforeDiscount.toString()", `is`(expectedTotalPriceBeforeDiscount.toString()),
                "totalDiscount.toString()", `is`(expectedTotalDiscount.toString()),
                "totalPrice.toString()", `is`(expectedTotalPrice.toString())
            )
    }

    @Transactional
    private fun givenProducts(vararg products: Product) {
        products.forEach { product ->
            productRepository.insertProduct(product)
        }
    }

    @Transactional
    private fun givenBasket(basket: Basket) {
        basketRepository.updateBasket(basket)
    }

    @Transactional
    private fun givenDiscounts(vararg discounts: Discount) {
        discounts.forEach { discount: Discount -> discountRepository.addDiscount(discount) }
    }

    companion object {
        private val basketBuilder: BasketBuilder = BasketBuilder()
        private val createDiscountDtoBuilder: CreateDiscountDtoBuilder = CreateDiscountDtoBuilder()

        val SAMPLE_PRODUCT_A = Product("product-a", "Product A", null, 5.99)
        val SAMPLE_PRODUCT_B = Product("product-b", "Product B", "Product B Description", 699.99)
        val SAMPLE_BASKET = basketBuilder
            .setProductCount(
                mutableMapOf(
                    "product-a" to 1,
                    "product-b" to 5
                )
            )
            .build()

        val DISCOUNT_A = createDiscountDtoBuilder
            .setId("product-a")
            .setProductCount(1)
            .setDiscountInPercentage(20)
            .setDescription("20% off on all product A")
            .build()
            .toDomain()
        val DISCOUNT_B = createDiscountDtoBuilder
            .setId("product-b")
            .setProductCount(2)
            .setDiscountInPercentage(10)
            .setDescription("Buy 2 get the second one 10% off")
            .build()
            .toDomain()
    }
}
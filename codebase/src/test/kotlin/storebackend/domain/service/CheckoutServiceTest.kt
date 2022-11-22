package storebackend.domain.service

import storebackend.adapters.input.rest.dto.CreateDiscountDtoBuilder
import storebackend.adapters.output.repository.InMemoryBasketRepositoryImpl
import storebackend.adapters.output.repository.InMemoryDiscountRepositoryImpl
import storebackend.adapters.output.repository.InMemoryProductRepositoryImpl
import storebackend.domain.Utils
import storebackend.domain.models.BasketBuilder
import storebackend.domain.models.Product
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.*

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CheckoutServiceTest {
    private val productRepositoryImpl = mockk<InMemoryProductRepositoryImpl>()
    private val basketRepositoryImpl = mockk<InMemoryBasketRepositoryImpl>()
    private val discountRepositoryImpl = mockk<InMemoryDiscountRepositoryImpl>()
    private val discountDtoBuilder: CreateDiscountDtoBuilder = CreateDiscountDtoBuilder()
    private val basketBuilder: BasketBuilder = BasketBuilder()
    private val checkoutService: CheckoutService =
        CheckoutService(basketRepositoryImpl, productRepositoryImpl, discountRepositoryImpl)

    @BeforeEach
    fun setUpBeforeEach() {
        every { productRepositoryImpl.getByProductId(PRODUCT_A.id) } returns PRODUCT_A
        every { productRepositoryImpl.getByProductId(PRODUCT_B.id) } returns PRODUCT_B
        every { productRepositoryImpl.getByProductId(PRODUCT_C.id) } returns PRODUCT_C
        every { discountRepositoryImpl.getDiscount(PRODUCT_A.id) } returns DISCOUNT_A
        every { discountRepositoryImpl.getDiscount(PRODUCT_B.id) } returns null
        every { discountRepositoryImpl.getDiscount(PRODUCT_C.id) } returns null
    }

    @Test
    fun `checkout (without discounts)`() {
        //given
        every { basketRepositoryImpl.getBasket() } returns basketBuilder
            .setProductCount(
                mutableMapOf(
                    PRODUCT_B.id to 1,
                    PRODUCT_C.id to 10
                )
            )
            .build()
        //when
        val receipt = checkoutService.checkout()
        //then
        val totalPriceBeforeDiscount = Utils.roundOffDecimal(PRODUCT_B.price * 1 + PRODUCT_C.price * 10)
        Assertions.assertEquals(totalPriceBeforeDiscount, receipt.totalPriceBeforeDiscount)
        Assertions.assertEquals(0.00, receipt.totalDiscount)
        Assertions.assertEquals(totalPriceBeforeDiscount, receipt.totalPrice)
    }

    @Test
    fun `checkout, with one discount`() {
        //given
        every { basketRepositoryImpl.getBasket() } returns basketBuilder
            .setProductCount(
                mutableMapOf(
                    PRODUCT_A.id to 5,
                    PRODUCT_B.id to 1,
                    PRODUCT_C.id to 10,
                )
            )
            .build()

        //when
        val receipt = checkoutService.checkout()

        //then
        val priceBeforeDiscount =
            Utils.roundOffDecimal(PRODUCT_A.price * 5 + PRODUCT_B.price * 1 + PRODUCT_C.price * 10)
        val discount =
            Utils.roundOffDecimal(PRODUCT_A.price * DISCOUNT_A.discountInPercentage * 0.01 * 5.floorDiv(DISCOUNT_A.productCount))
        Assertions.assertEquals(priceBeforeDiscount, receipt.totalPriceBeforeDiscount)
        Assertions.assertEquals(discount, receipt.totalDiscount)
        Assertions.assertEquals(priceBeforeDiscount - discount, receipt.totalPrice)
    }

    @Test
    fun `checkout, with more than one discount and one of the discount is applied more than once`() {
        //given
        every { basketRepositoryImpl.getBasket() } returns basketBuilder
            .setProductCount(
                mutableMapOf(
                    PRODUCT_A.id to 5,
                    PRODUCT_B.id to 1,
                    PRODUCT_C.id to 10,
                )
            )
            .build()
        every { discountRepositoryImpl.getDiscount(PRODUCT_C.id) } returns discountDtoBuilder
            .setId(PRODUCT_C.id)
            .setProductCount(3)
            .setDiscountInPercentage(50)
            .setDescription("Buy 3, get the 3rd 50 percent off")
            .build()
            .toDomain()

        //when
        val receipt = checkoutService.checkout()

        //then
        val expectedPriceBeforeDiscount =
            Utils.roundOffDecimal(PRODUCT_A.price * 5 + +PRODUCT_B.price * 1 + +PRODUCT_C.price * 10)
        val expectedDiscount = Utils.roundOffDecimal(
            PRODUCT_A.price * DISCOUNT_A.discountInPercentage * 0.01 * 5.floorDiv(DISCOUNT_A.productCount)
                    + PRODUCT_C.price * 50 * 0.01 * 10.floorDiv(3)
        )
        Assertions.assertEquals(expectedPriceBeforeDiscount, receipt.totalPriceBeforeDiscount)
        Assertions.assertEquals(expectedDiscount, receipt.totalDiscount)
        Assertions.assertEquals(expectedPriceBeforeDiscount - expectedDiscount, receipt.totalPrice)
    }

    companion object {
        private val discountDtoBuilder: CreateDiscountDtoBuilder = CreateDiscountDtoBuilder()

        val PRODUCT_A = Product(id = "product-a", name = "Product A", description = null, price = 1.99)
        val PRODUCT_B = Product(id = "product-b", name = "Product B", description = null, price = 599.99)
        val PRODUCT_C = Product(id = "product-c", name = "Product C", description = "Some descr. of C", price = 60.00)
        val DISCOUNT_A = discountDtoBuilder
            .setId(PRODUCT_A.id)
            .setProductCount(5)
            .setDiscountInPercentage(10)
            .setDescription("Buy 5 get 10 per cent off")
            .build()
            .toDomain()
    }
}
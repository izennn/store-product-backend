package storebackend.adapters.output.repository

import storebackend.domain.models.Discount
import storebackend.mockDB.DiscountDB
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.*
import javax.inject.Inject

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InMemoryDiscountRepositoryImplTest {
    @Inject
    private lateinit var database: DiscountDB

    @Inject
    private lateinit var implementer: InMemoryDiscountRepositoryImpl

    @BeforeEach
    fun setUp() {
        database.clearDB()
    }

    @AfterAll
    fun tearDown() {
        database.clearDB()
    }

    @Test
    fun `can retrieve an existing discount`() {
        //given
        givenDiscounts(Discount("product-b", productCount = 1, discountInPercentage = 5))
        val discountToAdd = Discount(
            productId = "product-a",
            productCount = 2,
            discountInPercentage = 50
        )
        //when
        implementer.addDiscount(discountToAdd)
        //then
        val discounts = database.retrieveDiscounts()
        Assertions.assertEquals(2, discounts.size)
    }

    @Test
    fun `add a discount rule`() {
        //given
        givenDiscounts(
            Discount("product-a", 2, 50),
            Discount("product-b", productCount = 1, discountInPercentage = 5)
        )
        //when
        val actual = implementer.getDiscount("product-b")
        //then
        Assertions.assertNotNull(actual)
        if (actual != null) {
            Assertions.assertEquals("product-b", actual.productId)
            Assertions.assertEquals(1, actual.productCount)
        }
    }

    @Test
    fun `update an existing discount`() {
        //given
        givenDiscounts(
            Discount("product-b", productCount = 1, discountInPercentage = 5)
        )
        val discountToAdd = Discount(
            productId = "product-b",
            productCount = 2,
            discountInPercentage = 9
        )
        //when
        implementer.addDiscount(discountToAdd)
        //then
        val discounts = database.retrieveDiscounts()
        Assertions.assertEquals(1, discounts.size)
        Assertions.assertNotNull(discounts["product-b"])
        discounts["product-b"]?.let { Assertions.assertEquals(2, it.productCount) }
        discounts["product-b"]?.let { Assertions.assertEquals(9, it.discountInPercentage) }
    }

    @Test
    fun `remove a discount`() {
        //given
        givenDiscounts(
            Discount("product-a", 2, 50),
            Discount("product-b", productCount = 1, discountInPercentage = 5)
        )
        //when
        implementer.deleteDiscount("product-b")
        //then
        val discounts = database.retrieveDiscounts()
        Assertions.assertEquals(1, discounts.size)
    }

    @Test
    fun `does not crash if removing a non-existent discount`() {
        //given
        givenDiscounts(
            Discount("product-a", 2, 50),
            Discount("product-b", productCount = 1, discountInPercentage = 5)
        )
        //when
        implementer.deleteDiscount("product-c")
        //then
        val discounts = database.retrieveDiscounts()
        Assertions.assertEquals(2, discounts.size)

    }

    private fun givenDiscounts(vararg discounts: Discount) {
        discounts.forEach { discount -> database.insertDiscount(discount) }
    }
}
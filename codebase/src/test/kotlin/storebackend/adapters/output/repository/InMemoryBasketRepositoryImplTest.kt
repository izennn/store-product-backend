package storebackend.adapters.output.repository

import storebackend.domain.models.Basket
import storebackend.domain.models.BasketBuilder
import storebackend.mockDB.BasketDB
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.*
import javax.inject.Inject
import javax.transaction.Transactional

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InMemoryBasketRepositoryImplTest {
    @Inject
    private lateinit var database: BasketDB

    @Inject
    private lateinit var implementer: InMemoryBasketRepositoryImpl

    private val basketBuilder: BasketBuilder = BasketBuilder()

    @Transactional
    @BeforeEach
    fun setUp() {
        database.clearDB()
    }

    @AfterAll
    fun tearDown() {
        database.clearDB()
    }

    @Test
    fun `add a new product`() {
        //when
        implementer.addProduct("product-a", 5)
        //then
        val actualCount = database.getProductCount()
        Assertions.assertEquals(1, database.getProductCount().size)
        Assertions.assertEquals(5, actualCount["product-a"])
    }

    @Test
    fun `remove a product`() {
        //given
        givenBasket(basketBuilder
            .setProductCount(mutableMapOf(
                "product-a" to 5,
                "product-b" to 2
            ))
            .build()
        )
        //when
        implementer.removeProduct("product-b", 3)
        //then
        val actualCount = database.getProductCount()
        Assertions.assertEquals(1, actualCount.keys.size)
    }

    @Test
    fun `increase a product count`() {
        //given
        givenBasket(basketBuilder
            .setProductCount(mutableMapOf(
                "product-a" to 5,
                "product-b" to 2
            ))
            .build()
        )        //when
        implementer.addProduct("product-a", 5)
        //then
        val actualCount = database.getProductCount()
        Assertions.assertEquals(10, actualCount["product-a"])
    }

    @Test
    fun `decrease a product count`() {
        //given
        givenBasket(basketBuilder
            .setProductCount(mutableMapOf(
                "product-a" to 5,
                "product-b" to 2
            ))
            .build()
        )        //when
        implementer.removeProduct("product-a", 3)
        //then
        val actualCount = database.getProductCount()
        Assertions.assertEquals(2, actualCount["product-a"])
    }

    @Transactional
    private fun givenBasket(basket: Basket) {
        database.updateBasket(basket)
    }
}
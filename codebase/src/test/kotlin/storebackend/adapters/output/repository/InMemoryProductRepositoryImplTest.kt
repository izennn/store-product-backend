package storebackend.adapters.output.repository

import storebackend.domain.models.Product
import storebackend.mockDB.ProductDB
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.*
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.BadRequestException

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InMemoryProductRepositoryImplTest {
    @Inject
    private lateinit var database: ProductDB

    @Inject
    private lateinit var implementer: InMemoryProductRepositoryImpl

    @BeforeEach
    fun setUp() {
        database.clearDB()
    }

    @AfterAll
    fun tearDown() {
        database.clearDB()
    }

    @Test
    fun `should be able to insert a product that does not exist yet`() {
        //given
        givenProductsExist(SAMPLE_PRODUCT_A, SAMPLE_PRODUCT_B)
        val newProduct = Product("new-product", "New Product", "Cool Thing", 1999.99)
        //when
        implementer.insertProduct(newProduct)
        //then
        val expectedSize = database.listAllProducts().size
        Assertions.assertEquals(expectedSize, 3)
    }

    @Test
    fun `should throw error when creating an already existing product`() {
        val exception = assertThrows<BadRequestException> {
            //given
            givenProductsExist(SAMPLE_PRODUCT_A, SAMPLE_PRODUCT_B)
            //when
            implementer.insertProduct(SAMPLE_PRODUCT_A)
        }

        Assertions.assertEquals("Product with productName=${SAMPLE_PRODUCT_A.name} already exists", exception.message)
    }

    @Test
    fun `should be able to delete an existing product`() {
        //given
        givenProductsExist(SAMPLE_PRODUCT_A, SAMPLE_PRODUCT_B)
        //when
        implementer.removeProduct(SAMPLE_PRODUCT_B.id)
        //then
        val expectedSize = database.listAllProducts().size
        Assertions.assertEquals(expectedSize, 1)
    }

    @Test
    fun `should throw error when deleting a non-existent product`() {
        val randomId = "random-product-id"
        val exception = assertThrows<BadRequestException> {
            //given
            givenProductsExist(SAMPLE_PRODUCT_A, SAMPLE_PRODUCT_B)
            //when
            implementer.removeProduct(randomId)
        }

        Assertions.assertEquals("Product with productId=${randomId} does not exist", exception.message)
    }

    @Transactional
    private fun givenProductsExist(vararg products: Product) {
        database.addProductsToDB(products.asList())
    }

    companion object {
        private val SAMPLE_PRODUCT_A = Product("sample-product-a", "Dishwasher", "Something to wash your dishes", 599.99)
        private val SAMPLE_PRODUCT_B = Product("sample-product-b", "Water", "Quench your thirst with this beverage", 1.99)
    }
}
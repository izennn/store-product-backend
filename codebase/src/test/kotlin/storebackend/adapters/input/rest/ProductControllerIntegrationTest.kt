package storebackend.adapters.input.rest

import storebackend.adapters.input.rest.dto.CreateProductDtoBuilder
import storebackend.adapters.output.repository.InMemoryProductRepositoryImpl
import storebackend.domain.models.Product
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.*
import javax.inject.Inject
import javax.transaction.Transactional

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductControllerIntegrationTest {
    @Inject
    private lateinit var createProductDtoBuilder: CreateProductDtoBuilder

    @Inject
    private lateinit var repository: InMemoryProductRepositoryImpl

    @BeforeEach
    fun setUp() {
        repository.clearAll()
    }

    @AfterAll
    fun tearDown() {
        repository.clearAll()
    }

    @Test
    fun `should be able to add a product`() {
        //given
        val productADto = createProductDtoBuilder
            .setName(name = SAMPLE_PRODUCT_A.name)
            .setDescription(description = SAMPLE_PRODUCT_A.description)
            .setPrice(price = SAMPLE_PRODUCT_A.price)
            .build()
        givenProductsExist(SAMPLE_PRODUCT_B)

        //when
        val response: Response = given()
            .contentType(ContentType.JSON)
            .and()
            .body(productADto)
            .`when`()
            .post("/products")

        //then
        response
            .then()
            .statusCode(200)
            .body(
                "id", notNullValue(),
                "name", `is`(SAMPLE_PRODUCT_A.name),
                "description", `is`(SAMPLE_PRODUCT_A.description),
                "price.toString()", `is`(SAMPLE_PRODUCT_A.price.toString()),
            )

        Assertions.assertEquals(repository.listAll().size, 2)
    }

    @Test
    fun `should be able to delete existing product`() {
        //given
        givenProductsExist(SAMPLE_PRODUCT_A, SAMPLE_PRODUCT_B)

        //when
        val response: Response = given()
            .`when`()
            .delete("/products/${SAMPLE_PRODUCT_B.id}")

        //then
        response
            .then()
            .statusCode(204)
        Assertions.assertEquals(1, repository.listAll().size)
    }

    @Test
    fun `should throw error when adding existing product`() {
        //given
        givenProductsExist(SAMPLE_PRODUCT_A, SAMPLE_PRODUCT_B)
        val createProductDto = createProductDtoBuilder
            .setName(name = SAMPLE_PRODUCT_A.name)
            .setDescription(description = SAMPLE_PRODUCT_A.description)
            .setPrice(price = SAMPLE_PRODUCT_A.price)
            .build()

        //when
        val response: Response = given()
            .contentType(ContentType.JSON)
            .and()
            .body(createProductDto)
            .`when`()
            .post("/products")

        //then
        //TODO: create global web exception mapper
        response
            .then()
            .statusCode(400)
    }

    @Test
    fun `should throw error when deleting non-existent product`() {
        //when
        val response: Response = given()
            .`when`()
            .delete("/products/${SAMPLE_PRODUCT_B.id}")

        //then
        response
            .then()
            .statusCode(400)
    }

    @Transactional
    private fun givenProductsExist(vararg products: Product) {
        for (product in products) {
            repository.insertProduct(product)
        }
    }

    companion object {
        private val SAMPLE_PRODUCT_A = Product("sample-product-a", "Dishwasher", "Something to wash your dishes", 599.99)
        private val SAMPLE_PRODUCT_B = Product("sample-product-b", "Water", "Quench your thirst with this beverage", 1.99)
    }
}
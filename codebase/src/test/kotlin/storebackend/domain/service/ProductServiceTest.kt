package storebackend.domain.service

import storebackend.adapters.input.rest.dto.CreateProductDtoBuilder
import storebackend.adapters.output.repository.InMemoryProductRepositoryImpl
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject

@QuarkusTest
class ProductServiceTest {
    @Inject
    private lateinit var repository: InMemoryProductRepositoryImpl

    @Inject
    private lateinit var server: ProductService

    @Inject
    private lateinit var createProductDtoBuilder: CreateProductDtoBuilder

    @BeforeEach
    fun setUp() {
        repository.clearAll()
    }

    @Test
    fun `can create product`() {
        val createProductDto = createProductDtoBuilder.build()
        server.createProduct(createProductDto.toDomain())
        Assertions.assertEquals(1, repository.listAll().size)
    }

    // deleteProduct method is just invoking repository method .removeProduct
    // hence for the sake of time will skip that testing as it's already covered in controller integration test
}

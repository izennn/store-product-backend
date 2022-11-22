package storebackend.adapters.input.rest.dto

import storebackend.TestUtils.SAMPLE_PRODUCT_DESCR
import storebackend.TestUtils.SAMPLE_PRODUCT_NAME
import storebackend.TestUtils.SAMPLE_PRODUCT_PRICE
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@QuarkusTest
class CreateProductDtoTest {
    private val createProductDtoBuilder: CreateProductDtoBuilder = CreateProductDtoBuilder()

    @Test
    fun `when converting product DTO to product model, an UUID is added`() {
        //when
        val actual = createProductDtoBuilder.build().toDomain()
        //then
        Assertions.assertNotNull(actual.id)
        Assertions.assertEquals(SAMPLE_PRODUCT_NAME, actual.name)
        Assertions.assertEquals(SAMPLE_PRODUCT_DESCR, actual.description)
        Assertions.assertEquals(SAMPLE_PRODUCT_PRICE, actual.price)
    }
}
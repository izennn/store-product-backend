package storebackend.adapters.input.rest.dto

import storebackend.TestUtils.SAMPLE_PRODUCT_DESCR
import storebackend.TestUtils.SAMPLE_PRODUCT_NAME
import storebackend.TestUtils.SAMPLE_PRODUCT_PRICE
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CreateProductDtoBuilder {
    private var dto = default()

    fun setName(name: String): CreateProductDtoBuilder {
        dto.name = name
        return this
    }

    fun setDescription(description: String?): CreateProductDtoBuilder {
        dto.description = description
        return this
    }

    fun setPrice(price: Double): CreateProductDtoBuilder {
        dto.price = price
        return this
    }

    fun build(): CreateProductDto {
        return dto
    }

    private fun default() = CreateProductDto(
        name = SAMPLE_PRODUCT_NAME,
        description = SAMPLE_PRODUCT_DESCR,
        price = SAMPLE_PRODUCT_PRICE
    )
}
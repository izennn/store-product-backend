package storebackend.adapters.input.rest.dto

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CreateDiscountDtoBuilder {
    private var dto = default()

    fun setId(newId: String): CreateDiscountDtoBuilder {
        dto.id = newId
        return this
    }

    fun setProductCount(count: Int): CreateDiscountDtoBuilder {
        dto.productCount = count
        return this
    }

    fun setDiscountInPercentage(percentage: Int): CreateDiscountDtoBuilder {
        dto.discountInPercentage = percentage
        return this
    }

    fun setDescription(description: String): CreateDiscountDtoBuilder {
        dto.description = description
        return this
    }

    fun build(): CreateDiscountDto {
        return dto
    }

    private fun default() = CreateDiscountDto(
        id = "sample-product-id",
        productCount = 1,
        discountInPercentage = 10,
        description = null
    )
}
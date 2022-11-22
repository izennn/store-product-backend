package storebackend.adapters.input.rest.dto

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class UpdateBasketDtoBuilder {
    private var dto = default()

    fun setProductCount(newProductCount: MutableMap<String, Int>): UpdateBasketDtoBuilder {
        dto.productCount = newProductCount
        return this
    }

    fun build(): UpdateBasketDto {
        return dto
    }

    private fun default() = UpdateBasketDto(
        productCount = mutableMapOf()
    )
}
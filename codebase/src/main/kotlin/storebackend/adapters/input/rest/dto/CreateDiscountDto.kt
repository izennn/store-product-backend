package storebackend.adapters.input.rest.dto

import storebackend.domain.models.Discount
import kotlinx.serialization.Serializable

@Serializable
data class CreateDiscountDto(
    var id: String,
    var productCount: Int,
    var discountInPercentage: Int,
    var description: String? = null
) {
    fun toDomain() = Discount(
        productId = id,
        productCount = productCount,
        discountInPercentage = discountInPercentage,
        description = description
    )
}
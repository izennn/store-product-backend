package storebackend.adapters.input.rest.dto

import storebackend.domain.models.Basket
import kotlinx.serialization.Serializable

@Serializable
data class UpdateBasketDto(
    var productCount: Map<String, Int>
) {
    fun toDomain(): Basket {
        return Basket(
            productCount = productCount as MutableMap<String, Int>
        )
    }
}
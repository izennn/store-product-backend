package storebackend.adapters.input.rest.dto

import storebackend.domain.models.Product
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class CreateProductDto(
    var name: String,
    var description: String?,
    var price: Double
) {
    fun toDomain(): Product {
        return Product(
            id = UUID.randomUUID().toString(),
            name = name,
            description = description,
            price = price
        )
    }
}
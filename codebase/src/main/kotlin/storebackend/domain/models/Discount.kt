package storebackend.domain.models

import kotlinx.serialization.Serializable

@Serializable
// assuming discount in percentage is applied to the "last" product
// e.g. buy 3 get last one free, would mean discountInPercentage = 100
data class Discount (
    val productId: String,
    val productCount: Int,
    val discountInPercentage: Int,
    val description: String? = null
)

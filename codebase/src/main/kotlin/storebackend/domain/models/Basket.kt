package storebackend.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Basket(
    var productCount: MutableMap<String, Int>
)
package storebackend.domain.ports.output

import storebackend.domain.models.Basket

interface UpdateBasketPort {
    fun updateBasket(newBasket: Basket): Basket
    fun addProduct(productId: String, count: Int)
    fun removeProduct(productId: String, count: Int)
}
package storebackend.domain.ports.input

import storebackend.domain.models.Product

interface CreatingProductUseCase {
    fun createProduct(product: Product): Product
}
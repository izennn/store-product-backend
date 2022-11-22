package storebackend.domain.ports.input

interface DeletingProductUseCase {
    fun deleteProduct(productId: String)
}
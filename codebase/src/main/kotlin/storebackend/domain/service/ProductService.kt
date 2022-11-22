package storebackend.domain.service

import storebackend.adapters.output.repository.InMemoryProductRepositoryImpl
import storebackend.domain.models.Product
import storebackend.domain.ports.input.CreatingProductUseCase
import storebackend.domain.ports.input.DeletingProductUseCase
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ProductService @Inject constructor(
    private val repository: InMemoryProductRepositoryImpl
) : CreatingProductUseCase, DeletingProductUseCase {
    override fun createProduct(product: Product): Product {
        repository.insertProduct(product)
        return product
    }

    override fun deleteProduct(productId: String) {
        repository.removeProduct(productId)
    }
}
package storebackend.domain.ports.output

import storebackend.domain.models.Product

interface InsertingProductPort {
    fun insertProduct(product: Product)
}
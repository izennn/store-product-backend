package storebackend.domain.ports.output

interface RemovingProductPort {
    fun removeProduct(productId: String)
}
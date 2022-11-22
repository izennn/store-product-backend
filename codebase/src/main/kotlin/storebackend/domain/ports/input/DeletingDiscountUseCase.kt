package storebackend.domain.ports.input

interface DeletingDiscountUseCase {
    fun deleteDiscount(id: String)
}
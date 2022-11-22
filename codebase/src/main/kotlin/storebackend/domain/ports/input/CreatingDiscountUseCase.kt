package storebackend.domain.ports.input

import storebackend.domain.models.Discount

interface CreatingDiscountUseCase {
    fun createDiscount(discount: Discount)
}
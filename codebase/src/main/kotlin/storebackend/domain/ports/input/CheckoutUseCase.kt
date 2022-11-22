package storebackend.domain.ports.input

import storebackend.domain.models.Receipt

interface CheckoutUseCase {
    fun checkout(): Receipt
}
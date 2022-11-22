package storebackend.domain.service

import storebackend.adapters.output.repository.InMemoryDiscountRepositoryImpl
import storebackend.domain.models.Discount
import storebackend.domain.ports.input.CreatingDiscountUseCase
import storebackend.domain.ports.input.DeletingDiscountUseCase
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class DiscountService @Inject constructor(
    val repository: InMemoryDiscountRepositoryImpl
) : CreatingDiscountUseCase, DeletingDiscountUseCase {
    override fun createDiscount(discount: Discount) {
        repository.addDiscount(discount)
    }

    override fun deleteDiscount(id: String) {
        repository.deleteDiscount(id)
    }
}
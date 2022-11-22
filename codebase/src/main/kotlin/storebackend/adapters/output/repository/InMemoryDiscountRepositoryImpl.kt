package storebackend.adapters.output.repository

import storebackend.domain.models.Discount
import storebackend.mockDB.DiscountDB
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class InMemoryDiscountRepositoryImpl @Inject constructor(
    private val database: DiscountDB
) {
    fun clearAll() {
        database.clearDB()
    }

    fun listDiscounts(): MutableMap<String, Discount> {
        return database.retrieveDiscounts()
    }

    fun getDiscount(productId: String): Discount? {
        return database.findDiscountByProductId(productId)
    }

    fun addDiscount(discount: Discount) {
        database.insertDiscount(discount)
    }

    fun deleteDiscount(productId: String) {
        database.removeDiscount(productId)
    }
}
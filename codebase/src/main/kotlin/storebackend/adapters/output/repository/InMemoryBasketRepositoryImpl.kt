package storebackend.adapters.output.repository

import storebackend.domain.models.Basket
import storebackend.domain.ports.output.UpdateBasketPort
import storebackend.mockDB.BasketDB
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class InMemoryBasketRepositoryImpl @Inject constructor(
    private val database: BasketDB
) : UpdateBasketPort {
    fun clearAll() {
        database.clearDB()
    }

    fun getBasket(): Basket {
        return database.getBasket()
    }

    override fun addProduct(productId: String, count: Int) {
        database.addProductCount(productId, count)
    }

    override fun removeProduct(productId: String, count: Int) {
        database.removeProductCount(productId, count)
    }

    override fun updateBasket(newBasket: Basket): Basket {
        database.updateBasket(newBasket)
        return newBasket
    }
}
package storebackend.mockDB

import storebackend.domain.models.Basket
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
object BasketDB {
    private val logger: Logger = LoggerFactory.getLogger(BasketDB::class.java)
    private var basket: Basket = Basket(
        productCount = mutableMapOf()
    )

    init {
        logger.info("Basket database singleton class invoked")
    }

    fun getBasket(): Basket {
        return basket
    }

    fun clearDB() {
        basket.productCount = mutableMapOf()
    }

    fun getProductCount(): MutableMap<String, Int> {
        return basket.productCount
    }

    fun addProductCount(productId: String, count: Int) {
        if (hasProduct(productId)) {
            val oldValue = basket.productCount[productId]
            if (oldValue != null) {
                basket.productCount[productId] = oldValue + count
            }
            return
        }

        basket.productCount[productId] = count
    }

    fun removeProductCount(productId: String, count: Int) {
        if (!hasProduct(productId)) {
            return
        }

        val oldValue = basket.productCount[productId]
        if (oldValue != null) {
            if (oldValue - count <= 0) {
                basket.productCount.remove(productId)
            } else {
                basket.productCount[productId] = oldValue - count
            }
        }
    }

    fun updateBasket(newBasket: Basket) {
        basket.productCount = newBasket.productCount
    }

    private fun hasProduct(productId: String): Boolean {
        return basket.productCount.containsKey(productId)
    }
}
package storebackend.mockDB

import storebackend.domain.models.Discount
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
object DiscountDB {
    private val logger: Logger = LoggerFactory.getLogger(BasketDB::class.java)
    private var discounts: MutableMap<String, Discount> = mutableMapOf()

    init {
        logger.info("Discount database singleton class invoked")
    }

    fun clearDB() {
        discounts = mutableMapOf()
    }

    fun findDiscountByProductId(productId: String): Discount? {
        return discounts[productId]
    }

    fun retrieveDiscounts(): MutableMap<String, Discount> {
        return discounts
    }

    fun insertDiscount(discount: Discount) {
        discounts[discount.productId] = discount
    }

    fun removeDiscount(productId: String) {
        if (findDiscountByProductId(productId) == null) {
            logger.info("Could not find discount for productId={}", productId)
            return
        }

        discounts.remove(productId)
    }
}
package storebackend.mockDB

import storebackend.domain.models.Product
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
object ProductDB {
    private val logger: Logger = LoggerFactory.getLogger(ProductDB::class.java)
    private val products: MutableList<Product> = mutableListOf()

    init {
        logger.info("Products database singleton class invoked")
    }

    fun clearDB() {
        products.clear()
    }

    fun listAllProducts(): List<Product> {
        return products
    }

    fun findById(productId: String): Product? {
        return products.find { product -> product.id == productId }
    }

    fun findByProduct(productToBeFound: Product): Product? {
        return products.find { product -> product.name == productToBeFound.name }
    }

    fun addProductToDB(product: Product) {
        products.add(product)
    }

    fun addProductsToDB(newProducts: List<Product>) {
        products.addAll(newProducts)
    }

    fun removeProductFromDB(productId: String) {
        products.removeIf { product -> product.id == productId}
    }
}
package storebackend.adapters.input.rest

import storebackend.adapters.input.rest.dto.CreateProductDto
import storebackend.domain.models.Product
import storebackend.domain.service.ProductService
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/products")
class ProductController @Inject constructor(
    val productService: ProductService
) {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun listProducts() {
        logger.info("action=listing all products")
    }

    @GET
    @Path("{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getProduct(
        @PathParam("productId") productId: String
    ) {
        logger.info("action=retrieving product with productId={}", productId)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createProduct(createProductDto: CreateProductDto): Product {
        logger.info("action=creating product={}", createProductDto.toString())
        val product = createProductDto.toDomain()
        return productService.createProduct(product)
    }

    @DELETE
    @Path("{productId}")
    fun deleteProduct(
        @PathParam("productId") productId: String
    ) {
        logger.info("action=deleting product with productId={}", productId)
        productService.deleteProduct(productId)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ProductController::class.java)
    }
}
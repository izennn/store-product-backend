package storebackend.adapters.input.rest

import storebackend.adapters.input.rest.dto.UpdateBasketDto
import storebackend.domain.models.Basket
import storebackend.domain.models.Receipt
import storebackend.domain.service.BasketService
import storebackend.domain.service.CheckoutService
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/basket")
class BasketController @Inject constructor(
    private val basketService: BasketService,
    private val checkoutService: CheckoutService
) {
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun modifyBasket(@RequestBody updateBasketDto: UpdateBasketDto): Basket {
        storebackend.adapters.input.rest.BasketController.Companion.logger.info("action=modifying basket, payload={}", updateBasketDto.toString())
        return basketService.modifyBasket(updateBasketDto)
    }

    @GET
    @Path("/checkout")
    @Produces(MediaType.APPLICATION_JSON)
    fun checkout(): Receipt {
        storebackend.adapters.input.rest.BasketController.Companion.logger.info("action=checking out basket, calculating price")
        return checkoutService.checkout()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(storebackend.adapters.input.rest.BasketController::class.java)
    }
}
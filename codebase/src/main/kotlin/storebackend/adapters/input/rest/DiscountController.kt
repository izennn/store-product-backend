package storebackend.adapters.input.rest

import storebackend.adapters.input.rest.dto.CreateDiscountDto
import storebackend.domain.service.DiscountService
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/discount")
class DiscountController @Inject constructor(
    private val discountService: DiscountService
) {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun addDiscount(@RequestBody createDiscountDto: CreateDiscountDto) {
        val discount = createDiscountDto.toDomain()
        discountService.createDiscount(discount)
    }

    @DELETE
    @Path("{discountId}")
    fun removeDiscount(
        @PathParam("discountId") discountId: String
    ) {
        discountService.deleteDiscount(discountId)
    }
}
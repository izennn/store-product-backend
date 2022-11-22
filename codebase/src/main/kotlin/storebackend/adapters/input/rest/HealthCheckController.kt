package storebackend.adapters.input.rest

import storebackend.domain.ports.input.HealthCheckingUseCase
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/hello")
class HealthCheckController @Inject constructor(
    private val healthCheckService: HealthCheckingUseCase
) {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun greeting(): String {
        return healthCheckService.greet()
    }
}
package storebackend.domain.service

import javax.enterprise.context.ApplicationScoped
import storebackend.domain.ports.input.HealthCheckingUseCase

@ApplicationScoped
class HealthCheckingService : HealthCheckingUseCase {
    override fun greet(): String {
        return "Greetings from Quarkus!!"
    }
}
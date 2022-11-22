import storebackend.domain.service.HealthCheckingService

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions;
import javax.inject.Inject


@QuarkusTest
class HealthCheckingServiceTest() {
    @Inject
    private lateinit var server: HealthCheckingService

    @Test
    fun `should return string when greeting`() {
        //given
        val expected = "Greetings from Quarkus!!"
        //when
        val result = server.greet()
        //then
        Assertions.assertEquals(expected, result)
    }
}
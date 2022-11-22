package storebackend.adapters.input.rest

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
class HealthCheckControllerIntegrationTest {
    @Test
    fun `returns greeting on health check`() {
        //given
        given()
            //when
            .`when`()
            .get("/hello")
            //then
            .then()
            .statusCode(200)
            .body(`is`("Greetings from Quarkus!!"))
    }
}
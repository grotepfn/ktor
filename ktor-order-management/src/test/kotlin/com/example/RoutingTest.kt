package com.example

import com.example.plugins.configureRouting
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

class RoutingTest {


    @Test
    fun testRoot() = testApplication {

        val orders = listOf(
            Order(1, 1, OrderState.PAID),
            Order(2, 2, OrderState.CREATED)
        )

        val service = mockk<Service>()
        coEvery { service.getOrders() } returns orders


        //val response = client.post("/orders") {
        //  contentType(ContentType.Application.Json)
        // setBody(OrderDto(1, 1))
        // }

        val response = client.get("/orders")
        assertEquals(HttpStatusCode.OK, response.status)


    }


    @Test
    fun testRoot2() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }


        //val response = client.post("/orders") {
        //  contentType(ContentType.Application.Json)
        // setBody(OrderDto(1, 1))
        // }

        val response = client.get("/orders") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }
}
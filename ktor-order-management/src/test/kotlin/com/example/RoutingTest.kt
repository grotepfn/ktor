package com.example

//import io.ktor.server.plugins.contentnegotiation.*
import com.example.models.Order
import com.example.models.OrderDto
import com.example.models.OrderState
import com.example.models.PaymentDto
import com.example.plugins.Service
import com.example.plugins.configureRouting
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

class RoutingTest {


    @Test
    fun getOrders() = testApplication {

        install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
            gson()
        }


        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        //given
        val orders = listOf(
            Order(1, 1, OrderState.PAID),
            Order(2, 2, OrderState.CREATED)
        )

        val service = mockk<Service>()
        coEvery { service.getOrders() } returns orders


        application {
            configureRouting(service)
        }


        //when
        val response = client.get("/orders") {
            accept(ContentType.Application.Json)
        }

        //then
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(response.body(), orders)

    }


    @Test
    fun createOrder() = testApplication {

        install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
            gson()
        }


        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        //given
        val order = Order(2, 2, OrderState.CREATED)

        val service = mockk<Service>()
        coEvery { service.addNewOrder(OrderDto(2, 2)) } returns order


        application {
            configureRouting(service)
        }


        //when
        val response = client.post("/orders") {
            contentType(ContentType.Application.Json)
            setBody(OrderDto(2, 2))
            accept(ContentType.Application.Json)
        }

        //then
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(response.body(), order)

    }

    @Test
    fun processPayment_Succeeds() = testApplication {

        install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
            gson()
        }


        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        //given
        val order = Order(2, 2, OrderState.CREATED)

        val service = mockk<Service>()
        coEvery { service.getOrderById(2) } returns order
        every { service.processPayment(PaymentDto(2, true)) } answers {}

        application {
            configureRouting(service)
        }


        //when
        val response = client.post("/orders/payment") {
            contentType(ContentType.Application.Json)
            setBody(PaymentDto(2, true))
            accept(ContentType.Application.Json)
        }

        //then
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(response.body(), order)

    }


    @Test
    fun processPayment_Order_Id_Does_Not_Exist() = testApplication {

        install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
            gson()
        }


        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        //given
        val order = Order(2, 2, OrderState.CREATED)

        val service = mockk<Service>()
        coEvery { service.getOrderById(2) } returns null


        application {
            configureRouting(service)
        }


        //when
        val response = client.post("/orders/payment") {
            contentType(ContentType.Application.Json)
            setBody(PaymentDto(2, true))
            accept(ContentType.Application.Json)
        }

        //then
        assertEquals(HttpStatusCode.BadRequest, response.status)


    }


    @Test
    fun processPayment_Order_Not_Status_Created() = testApplication {

        install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
            gson()
        }


        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        //given
        val order = Order(2, 2, OrderState.PAID)

        val service = mockk<Service>()
        coEvery { service.getOrderById(2) } returns order


        application {
            configureRouting(service)
        }


        //when
        val response = client.post("/orders/payment") {
            contentType(ContentType.Application.Json)
            setBody(PaymentDto(2, true))
            accept(ContentType.Application.Json)
        }

        //then
        assertEquals(HttpStatusCode.BadRequest, response.status)


    }


    @Test
    fun getFulfillment() = testApplication {

        install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
            gson()
        }


        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        //given
        val order = Order(2, 2, OrderState.PAID)

        val service = mockk<Service>()
        coEvery { service.getOrderById(2) } returns order


        application {
            configureRouting(service)
        }


        //when
        val response = client.get("/orders/fulfillment") {
            accept(ContentType.Application.Json)
        }

        //then
        assertEquals(HttpStatusCode.OK, response.status)


    }


}
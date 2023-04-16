package com.example

import com.example.plugins.Service
import io.ktor.client.*
import io.ktor.server.testing.*
import org.junit.Test
import org.mockito.Mockito

class ServiceTest {


    @Test
     fun processPayment() = testApplication{


        val httpClient = Mockito.mock(HttpClient()::class.java)
        //Mockito.`when`(dao.updateOrder(1, OrderState.PAID))


        val service = Service(httpClient)

        service.getOrders()

        // service.processPayment(PaymentDto(orderId = 1, success = true)

    }
}

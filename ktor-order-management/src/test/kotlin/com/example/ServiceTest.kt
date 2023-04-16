package com.example

import io.ktor.client.*
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class ServiceTest {


    @Test
    fun processPayment() {


        val httpClient = Mockito.mock(HttpClient()::class.java)
        //Mockito.`when`(dao.updateOrder(1, OrderState.PAID))



        val service = Service(httpClient)

       // service.processPayment(PaymentDto(orderId = 1, success = true)

    }
}
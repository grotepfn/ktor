package com.example.plugins

import com.example.dao
import com.example.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*

class Service(
    private val client: io.ktor.client.HttpClient
) {


    suspend fun getOrders(): List<Order> {
        return dao.allOrders()
    }

    suspend fun getOrderById(id : Int): Order?{
        return dao.findOrderById(id)
    }

    suspend fun getFulfillment(): Boolean {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                gson()
            }
        }

        val fulfillmentDto: FulfillmentDto = client.get("http://127.0.0.1:8080/orders/fulfillment").body()


        return fulfillmentDto.success
    }


    suspend fun fullfilmentBatchProcess() {

        System.out.println("fullfilment")

        val orders: List<Order> = dao.getPaidOrders();

        for (order in orders) {
            if (getFulfillment()) {
                dao.updateOrder(order.id, OrderState.CLOSED)
            }
        }
    }


    suspend fun addNewOrder(orderDto: OrderDto): Order? {
        return dao.addNewOrder(orderDto.customerId)
    }

    fun processPayment(paymentDto: PaymentDto) {


        if (paymentDto.success) {
            dao.updateOrder(paymentDto.orderId, OrderState.PAID)
        }

    }


}
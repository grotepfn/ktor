package com.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.gson.*
import org.jetbrains.exposed.sql.transactions.transaction

class Service(
    private val client: io.ktor.client.HttpClient
) {


    suspend fun getFulfillment(): Boolean {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                gson()
            }
        }


        val fulfillmentDto: FulfillmentDto = client.get("http://127.0.0.1:8080/orders/fulfillment").body()


        return fulfillmentDto.success
    }


    suspend fun fullfilmentBatchProcess()  {

        System.out.println("fullfilment")

        val orders : List<Order> = dao.getPaidOrders();


        for(order in orders){
                if(getFulfillment()){
                    dao.updateOrder(order.id, OrderState.CLOSED)
                }
        }

    }


}
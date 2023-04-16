package com.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class Service(
    private val client: io.ktor.client.HttpClient
) {


    suspend fun getFulfillment(): Boolean {
        val client = HttpClient(CIO){
            install(JsonFeature)
        }

        val response: HttpResponse = client.get("http://127.0.0.1:8080/orders/fulfillment")

        val fulfillmentDto: FulfillmentDto = response.body()

        return fulfillmentDto.success
    }


}
package com.example

import org.jetbrains.exposed.sql.insert
import java.net.http.HttpClient

class Service (
        private val client = HttpClient(CIO) {
            install(Logging) {
                level = LogLevel.INFO
            }
        }
) {


    suspend fun getX(): Boolean{

       val result: FulfillmentDto =   client.re {

           url("http://127.0.0.1:8080/orders/fulfillment")
       }

    }
}
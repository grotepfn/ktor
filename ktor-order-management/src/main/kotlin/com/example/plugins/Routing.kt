package com.example.plugins

import com.example.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.net.http.HttpClient
import kotlin.random.Random

fun Application.configureRouting(
) {


    routing {
        post("/orders") {
            val order = call.receive(OrderDto::class)
            val customer2 = dao.addNewOrder(order.customerId)
            call.respondText("Hello World!")
        }


        get("/orders") {
            call.respond(dao.allOrders())
        }



        post("/orders/payment") {
             val paymentDto=  call.receive(PaymentDto::class)
            if(paymentDto.success){
                dao.updateOrder(paymentDto.orderId, OrderState.PAID)
            }


            val FulfillmentDto =  client.get("https://ktor.io/docs/welcome.html")


            call.respondText("Hello World!")
        }


        get("/orders/fulfillment") {

            call.respond(FulfillmentDto(Random.nextBoolean()))
        }
    }
}

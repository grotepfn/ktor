package com.example.plugins

import com.example.model.FulfillmentDto
import com.example.model.OrderDto
import com.example.model.OrderState
import com.example.model.PaymentDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

fun Application.configureRouting(
    service: Service
) {


    routing {
        post("/orders") {
            val orderDto = call.receive(OrderDto::class)
            val order = service.addNewOrder(orderDto)
            call.respond(order!!)
        }

        get("/orders") {
            call.respond(service.getOrders())
        }

        post("/orders/payment") {
            val paymentDto = call.receive(PaymentDto::class)

            val order = service.getOrderById(paymentDto.orderId)

            if (order == null || (order != null && order.state != OrderState.CREATED)) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            service.processPayment(paymentDto)

            //nicht cool
            call.respond(service.getOrderById(paymentDto.orderId)!!)
        }


        //callback from external source
        get("/orders/fulfillment") {
            call.respond(FulfillmentDto(Random.nextBoolean()))
        }

        post("/timer") {

            CoroutineScope(Job()).launch {
                repeat(10000) {
                    service.fullfilmentBatchProcess()
                    delay(10000)
                }
            }


        }


    }


}

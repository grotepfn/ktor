package com.example.plugins

import com.example.*
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
            val order = dao.addNewOrder(orderDto.customerId)
            call.respond(order!!)
        }

        get("/orders") {
            call.respond(dao.allOrders())
        }

        post("/orders/payment") {
            val paymentDto = call.receive(PaymentDto::class)

            val order = dao.findOrderById(paymentDto.orderId)

            if (order == null || (order != null && order.state != OrderState.CREATED)) {
                call.respond(HttpStatusCode.BadRequest)
            }

            service.processPayment(paymentDto)

            //nicht cool
            call.respond(dao.findOrderById(paymentDto.orderId)!!)
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

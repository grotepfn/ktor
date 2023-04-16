package com.example.plugins

import com.example.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.net.http.HttpClient
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
            val paymentDto=  call.receive(PaymentDto::class)

            val order = dao.order(paymentDto.orderId)


            if(order == null || (order != null && order.state != OrderState.CREATED)){
                call.respond(HttpStatusCode.BadRequest)
            }

            if(paymentDto.success){
                dao.updateOrder(paymentDto.orderId, OrderState.PAID)

                dao.updateOrder(paymentDto.orderId, OrderState.IN_FULFILLMENT)

                if(service.getFulfillment()){
                    dao.updateOrder(paymentDto.orderId, OrderState.CLOSED)
                }


            }



            call.respond(dao.order(paymentDto.orderId)!!)
        }


        get("/orders/fulfillment") {

            call.respond(FulfillmentDto(Random.nextBoolean()))
        }
    }
}

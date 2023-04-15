package com.example.plugins

import com.example.Order
import com.example.dao
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.freemarker.*

fun Application.configureRouting() {


    routing {
        post("/order") {
            val customer = call.receive(Order::class)
            System.out.println(customer.name)
            val customer2 = dao.addNewOrder(customer.name)
            call.respondText("Hello World!")
        }


        get("/order") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("articles" to dao.allArticles())))
        }
    }
}

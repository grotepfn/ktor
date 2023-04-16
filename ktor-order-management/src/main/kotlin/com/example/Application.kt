package com.example

import com.example.plugins.configureRouting
import io.ktor.client.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import java.util.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

}

fun Application.module() {
    configureRouting(Service(HttpClient()))
    install(ContentNegotiation) {
        gson()
    }
    DatabaseFactory.init()

    //configureTemplating()
}

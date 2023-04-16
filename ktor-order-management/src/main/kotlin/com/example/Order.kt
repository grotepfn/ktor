package com.example

import org.jetbrains.exposed.sql.*

data class Order(val id: Int, val customerId: Int, val state: OrderState)

object Orders : Table() {
    val id = integer("id").autoIncrement()
    val customerId = integer("customerId")
    val state = enumerationByName<OrderState>("state", 50)

}

enum class OrderState {
    CREATED, PAID, IN_FULFILLMENT, CLOSED
}
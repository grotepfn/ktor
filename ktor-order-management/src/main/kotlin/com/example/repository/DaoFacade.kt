package com.example

import com.example.model.Order
import com.example.model.OrderState
import com.example.model.Orders
import com.example.repository.DatabaseFactory.dbQuery
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class DaoFacade {


    private fun resultRowToOrder(row: ResultRow) = Order(
            id = row[Orders.id],
            customerId = row[Orders.customerId],
            state = row[Orders.state]
    )

    suspend fun allOrders(): List<Order> = dbQuery {
        Orders.selectAll().map(::resultRowToOrder)
    }

    suspend fun addNewOrder(customerId: Int): Order? = dbQuery {
        val insertStatement = Orders.insert {
            it[Orders.customerId] = customerId
            it[state] = OrderState.CREATED
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToOrder)
    }

    suspend fun findOrderById(id: Int): Order? = dbQuery {
        Orders
            .select { Orders.id eq id }
            .map(::resultRowToOrder)
            .singleOrNull()
    }


    suspend fun getPaidOrders(): List<Order> = dbQuery {
        Orders
            .select { Orders.state eq OrderState.PAID }
            .map(::resultRowToOrder)
    }


    fun updateOrder(id: Int, state: OrderState) {
        transaction {
            Orders.update({ Orders.id eq id }) {
                it[Orders.state] = state
            }
        }
    }

}

val dao: DaoFacade = DaoFacade().apply {
    runBlocking {

    }
}
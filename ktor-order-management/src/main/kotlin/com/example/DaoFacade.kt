package com.example

import com.example.DatabaseFactory.dbQuery
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
            it[Orders.state] = OrderState.CREATED
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToOrder)
    }

    suspend fun order(id: Int): Order? = dbQuery {
        Orders
            .select { Orders.id eq id }
            .map(::resultRowToOrder)
            .singleOrNull()
    }


    suspend fun updateOrder(id: Int, state: OrderState) {
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
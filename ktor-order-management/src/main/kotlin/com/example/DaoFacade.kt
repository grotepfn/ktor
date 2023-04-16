package com.example

import com.example.DatabaseFactory.dbQuery
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

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


    suspend fun updateOrder(id: Int, state: OrderState) {
        val updateStaement = transaction {
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
package com.example

import com.example.DatabaseFactory.dbQuery
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class DaoFacade {


    private fun resultRowToOrder(row: ResultRow) = Order(
        id = row[Orders.id],
        name = row[Orders.name]
    )

    suspend fun allOrders(): List<Order> = dbQuery {
        Orders.selectAll().map(::resultRowToOrder)
    }

     suspend fun addNewOrder(name: String): Order? = dbQuery {
        val insertStatement = Orders.insert {
            it[Orders.name] = name
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToOrder)
    }

}

val dao: DaoFacade = DaoFacade().apply {
    runBlocking {

    }
}
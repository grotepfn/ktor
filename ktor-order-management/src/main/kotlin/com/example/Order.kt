package com.example

import org.jetbrains.exposed.sql.*

data class Order(val id: Int, val name: String)

object Orders : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)

    override val primaryKey = PrimaryKey(id)
}
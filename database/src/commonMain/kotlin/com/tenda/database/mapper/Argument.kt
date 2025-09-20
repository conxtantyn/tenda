package com.tenda.database.mapper

import uniffi.database.Argument

fun List<Any?>.mapFromDomain(): List<Argument> =
    this.map { value ->
        when (value) {
            null -> Argument.Null
            is Int -> Argument.Int(value.toLong())
            is Long -> Argument.Int(value)
            is Short -> Argument.Int(value.toLong())
            is Byte -> Argument.Int(value.toLong())
            is Float -> Argument.Real(value.toDouble())
            is Double -> Argument.Real(value)
            is String -> Argument.Text(value)
            else -> throw IllegalArgumentException("Unsupported type: ${value::class}")
        }
    }

package com.tenda.database.mapper

import uniffi.database.Entry

fun List<Any?>.mapFromDomain(): List<Entry> =
    this.map { value ->
        when (value) {
            null -> Entry.Null
            is Int -> Entry.Int(value.toLong())
            is Long -> Entry.Int(value)
            is Short -> Entry.Int(value.toLong())
            is Byte -> Entry.Int(value.toLong())
            is Float -> Entry.Real(value.toDouble())
            is Double -> Entry.Real(value)
            is String -> Entry.Text(value)
            else -> throw IllegalArgumentException("Unsupported type: ${value::class}")
        }
    }

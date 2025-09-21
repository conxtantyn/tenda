package com.tenda.database.model

import kotlinx.serialization.Serializable

@Serializable
data class ExceptionModel(
    val status: String,
    val reason: String? = null,
)

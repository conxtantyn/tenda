package com.tenda.database.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ResponseModel(
    val changes: Long,
    val data: List<JsonElement>
)

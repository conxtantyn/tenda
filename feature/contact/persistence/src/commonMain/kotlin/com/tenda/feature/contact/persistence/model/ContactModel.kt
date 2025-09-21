package com.tenda.feature.contact.persistence.model

import kotlinx.serialization.Serializable

@Serializable
data class ContactModel(
    val id: String,
    val name: String,
    val logo: String,
)

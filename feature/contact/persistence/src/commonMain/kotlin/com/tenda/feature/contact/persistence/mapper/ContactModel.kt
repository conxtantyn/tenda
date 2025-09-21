package com.tenda.feature.contact.persistence.mapper

import com.tenda.feature.contact.domain.model.Contact
import com.tenda.feature.contact.persistence.model.ContactModel

fun ContactModel.mapToDomain(): Contact {
    return Contact(
        id = id,
        name = name,
        logo = logo
    )
}

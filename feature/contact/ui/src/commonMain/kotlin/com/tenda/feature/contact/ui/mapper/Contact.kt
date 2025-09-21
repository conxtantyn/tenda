package com.tenda.feature.contact.ui.mapper

import com.tenda.feature.contact.domain.model.Contact
import com.tenda.feature.contact.ui.model.UiContact

fun Contact.mapFromDomain(): UiContact {
    return UiContact(
        id = this.id,
        name = this.name,
        logo = this.logo
    )
}

package com.tenda.feature.contact.persistence

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    value = [
        "com.tenda.feature.contact.persistence.usecase",
        "com.tenda.feature.contact.persistence.delegate"
    ]
)
class ContactPersistentModule

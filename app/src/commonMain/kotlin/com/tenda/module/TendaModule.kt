package com.tenda.module

import com.tenda.database.DatabaseModule
import com.tenda.feature.contact.data.ContactDataModule
import com.tenda.feature.contact.domain.ContactModule
import com.tenda.feature.contact.persistence.ContactPersistentModule
import com.tenda.persistence.core.PersistenceModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        CoreModule::class,
        DatabaseModule::class,
        PersistenceModule::class,
        ContactPersistentModule::class,
        ContactDataModule::class,
        ContactModule::class
    ]
)
@ComponentScan("com.tenda.usecase")
object TendaModule

package com.tenda.database

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import uniffi.database.Persistence
import uniffi.database.PersistenceInterface

@Module
@ComponentScan("com.tenda.database.repository")
class DatabaseModule {
    @Single
    fun provideInteractor(): PersistenceInterface {
        return Persistence()
    }
}

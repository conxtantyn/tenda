package com.tenda.module

import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class CoreModule {
    @Single
    fun provideJson(): Json = Json
}

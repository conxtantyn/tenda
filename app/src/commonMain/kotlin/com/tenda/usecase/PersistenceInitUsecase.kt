package com.tenda.usecase

import com.tenda.common.usecase.SuspendUseCase
import com.tenda.feature.contact.data.persistence.ContactPersistence
import org.koin.core.annotation.Factory

@Factory
class PersistenceInitUsecase(
    private val persistence: ContactPersistence
) : SuspendUseCase<Unit> {
    override suspend fun invoke() {
        persistence.initialize()
    }
}

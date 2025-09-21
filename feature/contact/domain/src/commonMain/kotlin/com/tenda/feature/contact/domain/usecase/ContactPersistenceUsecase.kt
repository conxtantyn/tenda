package com.tenda.feature.contact.domain.usecase

import com.tenda.common.usecase.SuspendUseCase
import com.tenda.feature.contact.domain.repository.ContactRepository
import org.koin.core.annotation.Factory

@Factory
class ContactPersistenceUsecase(
    private val repository: ContactRepository
) : SuspendUseCase<Unit> {
    override suspend fun invoke() {
        repository.initialize()
    }
}

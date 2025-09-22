package com.tenda.persistence.core.usecase

import com.tenda.common.usecase.SuspendUseCase
import com.tenda.persistence.core.repository.PersistenceRepository
import org.koin.core.annotation.Factory

@Factory
class SynchroniseUsecase(
    private val repository: PersistenceRepository
) : SuspendUseCase<Unit> {
    override suspend fun invoke() {
        repository.synchronise()
    }
}

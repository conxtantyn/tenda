package com.tenda.persistence.core.usecase

import com.tenda.common.usecase.SuspendWithArgsUseCase
import com.tenda.persistence.core.repository.PersistenceRepository
import org.koin.core.annotation.Factory

@Factory
class PersistenceUsecase(
    private val repository: PersistenceRepository
) : SuspendWithArgsUseCase<PersistenceUsecase.Argument, Unit> {
    override suspend fun invoke(args: Argument) {
        repository.open(
            url = args.url,
            token = args.token,
            database = args.database
        )
    }

    data class Argument(
        val url: String,
        val token: String,
        val database: String
    )
}

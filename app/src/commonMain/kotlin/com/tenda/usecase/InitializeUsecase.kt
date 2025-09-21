package com.tenda.usecase

import com.tenda.common.usecase.SuspendWithArgsUseCase
import com.tenda.feature.contact.domain.usecase.ContactPersistenceUsecase
import com.tenda.persistence.core.usecase.PersistenceUsecase
import org.koin.core.annotation.Factory

@Factory
class InitializeUsecase(
    private val persistenceUsecase: PersistenceUsecase,
    private val contactPersistenceUsecase: ContactPersistenceUsecase
) : SuspendWithArgsUseCase<InitializeUsecase.Argument, Unit> {
    override suspend fun invoke(args: Argument) {
        persistenceUsecase(
            PersistenceUsecase.Argument(
                url = args.url,
                token = args.token,
                database = args.database
            )
        )
        contactPersistenceUsecase()
    }

    data class Argument(
        val url: String,
        val token: String,
        val database: String
    )
}

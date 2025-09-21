package com.tenda.feature.contact.domain.usecase

import com.tenda.common.usecase.SuspendWithArgsUseCase
import com.tenda.feature.contact.domain.model.Contact
import com.tenda.feature.contact.domain.repository.ContactRepository
import org.koin.core.annotation.Factory

@Factory
class CreateDraftUsecase(
    private val repository: ContactRepository
) : SuspendWithArgsUseCase<String, Contact> {
    override suspend fun invoke(args: String): Contact {
        return repository.create(args)
    }
}

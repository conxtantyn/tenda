package com.tenda.feature.contact.domain.usecase

import com.tenda.common.schema.Paging
import com.tenda.common.usecase.SuspendWithArgsUseCase
import com.tenda.feature.contact.domain.model.Contact
import com.tenda.feature.contact.domain.repository.ContactRepository
import org.koin.core.annotation.Factory

@Factory
class ContactListUsecase(
    private val repository: ContactRepository
) : SuspendWithArgsUseCase<Paging.Request, Paging.Response<Contact>> {
    override suspend fun invoke(args: Paging.Request): Paging.Response<Contact> {
        return repository.getAll(args)
    }
}

package com.tenda.feature.contact.ui.contacts

import com.tenda.feature.contact.domain.usecase.ContactListUsecase
import com.tenda.ui.core.component.UiComponent
import org.koin.core.annotation.Module
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.ksp.generated.module

@Module
object Contacts {
    @org.koin.core.annotation.Scope(Contacts::class)
    fun provideContactsViewModel(usecase: ContactListUsecase): ContactsViewModel {
        return ContactsViewModel(usecase)
    }

    class Builder(scope: Scope): UiComponent.ComponentBuilder(scope) {
        override fun build(): Scope {
            val scope = scope(named<Contacts>())
            scope.getKoin().loadModules(listOf(
                Contacts.module
            ))
            return scope
        }
    }
}

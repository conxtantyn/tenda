package com.tenda.ui.home

import com.tenda.feature.contact.ui.contacts.Contacts
import com.tenda.feature.contact.ui.draft.Draft
import com.tenda.feature.contact.ui.draft.DraftEvent
import com.tenda.ui.core.component.UiComponent
import com.tenda.ui.core.component.UiComponentProvider
import com.tenda.ui.core.factory.UiBuilderFactory
import org.koin.core.annotation.Module
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.koin.ksp.generated.module

@Module
object Home {
    @org.koin.core.annotation.Scope(Home::class)
    fun provideHomeInteractor(): HomeInteractor {
        return HomeInteractor()
    }

    class Builder(scope: Scope): UiComponent.ComponentBuilder(scope) {
        override fun build(): Scope {
            val scope = scope(named<Home>())
            scope.getKoin().loadModules(listOf(
                module {
                    scope<Home> {
                        factory<DraftEvent> { get<HomeInteractor>() }
                        factory<UiComponentProvider.Factory> {
                            UiBuilderFactory(
                                listOf(
                                    this@Builder,
                                    Draft.Builder(scope),
                                    Contacts.Builder(scope),
                                )
                            )
                        }
                    }
                },
                Home.module
            ))
            return scope
        }
    }
}

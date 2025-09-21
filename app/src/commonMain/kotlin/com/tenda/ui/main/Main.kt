package com.tenda.ui.main

import com.tenda.module.TendaModule
import com.tenda.ui.core.component.UiComponent
import com.tenda.ui.core.component.UiComponentProvider
import com.tenda.ui.core.factory.UiBuilderFactory
import com.tenda.ui.home.Home
import com.tenda.ui.setup.Setup
import org.koin.core.annotation.Module
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.koin.ksp.generated.module

@Module
object Main {
    class Builder(scope: Scope): UiComponent.ComponentBuilder(scope) {
        override fun build(): Scope {
            val scope = scope(named<Main>())
            scope.getKoin().loadModules(listOf(
                module {
                    scope<Main> {
                        factory<UiComponentProvider.Factory> {
                            UiBuilderFactory(
                                listOf(
                                    this@Builder,
                                    Setup.Builder(scope),
                                    Home.Builder(scope),
                                )
                            )
                        }
                    }
                },
                TendaModule.module,
                Main.module
            ))
            return scope
        }
    }
}

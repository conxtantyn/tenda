package com.tenda.ui.home

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
    class Builder(scope: Scope): UiComponent.ComponentBuilder(scope) {
        override fun build(): Scope {
            val scope = scope(named<Home>())
            scope.getKoin().loadModules(listOf(
                module {
                    scope<Home> {
                        factory<UiComponentProvider.Factory> {
                            UiBuilderFactory(
                                listOf(
                                    this@Builder,
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

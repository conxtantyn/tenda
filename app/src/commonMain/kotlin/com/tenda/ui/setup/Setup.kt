package com.tenda.ui.setup

import com.tenda.ui.core.component.UiComponent
import com.tenda.ui.core.component.UiComponentProvider
import com.tenda.ui.core.factory.UiBuilderFactory
import com.tenda.usecase.InitializeUsecase
import org.koin.core.annotation.Module
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.koin.ksp.generated.module

@Module
object Setup {
    @org.koin.core.annotation.Scope(Setup::class)
    fun provideSettingsViewModel(usecase: InitializeUsecase): SetupViewModel {
        return SetupViewModel(usecase)
    }

    class Builder(scope: Scope): UiComponent.ComponentBuilder(scope) {
        override fun build(): Scope {
            val scope = scope(named<Setup>())
            scope.getKoin().loadModules(listOf(
                module {
                    scope<Setup> {
                        factory<UiComponentProvider.Factory> {
                            UiBuilderFactory(
                                listOf(
                                    this@Builder,
                                )
                            )
                        }
                    }
                },
                Setup.module
            ))
            return scope
        }
    }
}

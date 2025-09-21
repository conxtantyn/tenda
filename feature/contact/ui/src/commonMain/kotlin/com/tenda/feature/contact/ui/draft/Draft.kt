package com.tenda.feature.contact.ui.draft

import com.tenda.feature.contact.domain.usecase.CreateDraftUsecase
import com.tenda.ui.core.component.UiComponent
import org.koin.core.annotation.Module
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.ksp.generated.module

@Module
object Draft {
    @org.koin.core.annotation.Scope(Draft::class)
    fun provideSettingsViewModel(usecase: CreateDraftUsecase): DraftViewModel {
        return DraftViewModel(usecase)
    }

    class Builder(scope: Scope): UiComponent.ComponentBuilder(scope) {
        override fun build(): Scope {
            val scope = scope(named<Draft>())
            scope.getKoin().loadModules(listOf(
                Draft.module
            ))
            return scope
        }
    }
}

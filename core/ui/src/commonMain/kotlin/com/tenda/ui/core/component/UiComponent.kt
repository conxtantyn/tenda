package com.tenda.ui.core.component

import cafe.adriel.voyager.navigator.Navigator
import com.tenda.ui.core.extension.scopeOf
import org.koin.core.qualifier.TypeQualifier
import org.koin.core.scope.Scope

interface UiComponent {
    interface Builder

    abstract class Interactor {
        protected lateinit var scope: Scope

        open fun onAttach() {}
    }

    abstract class ScreenInteractor : Interactor() {
        protected lateinit var navigator: Navigator

        fun attach(scope: Scope, navigator: Navigator) {
            this.scope = scope
            this.navigator = navigator
            onAttach()
        }
    }

    abstract class ScreenInteractorWithArgs<T : Any> : Interactor() {
        protected lateinit var args: T

        fun attach(scope: Scope, args: T) {
            this.args = args
            this.scope = scope
            onAttach()
        }
    }

    abstract class ComponentBuilder(private val scope: Scope) : Builder {
        fun scope(qualifier: TypeQualifier): Scope {
            return scope.getKoin().scopeOf(qualifier)
        }

        abstract fun build(): Scope
    }
}

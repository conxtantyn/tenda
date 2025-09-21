package com.tenda.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.tenda.ui.setup.SetupScreen
import com.tenda.ui.core.extension.factory
import com.tenda.ui.core.theme.UiTheme
import org.koin.compose.getKoin
import org.koin.core.scope.ScopeID

class MainScreen(val scope: ScopeID) : Screen {
    @Composable
    override fun Content() {
        val component = factory(scope).builder(Main.Builder::class).build()
        UiTheme {
            Navigator(Launcher(component.id))
        }
    }
}

private class Launcher(val scope: ScopeID) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val currentScope = getKoin().getScope(scope)
        val interactor = currentScope.get<MainInteractor>()
        LaunchedEffect(Unit) {
            navigator?.let { interactor.attach(currentScope, navigator) }
        }
        SetupScreen(currentScope.id).Content()
    }
}

package com.tenda.ui.main

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.tenda.ui.core.extension.factory
import com.tenda.ui.core.theme.UiTheme
import com.tenda.ui.setup.SetupScreen
import org.koin.core.scope.ScopeID

class MainScreen(val scope: ScopeID) : Screen {
    @Composable
    override fun Content() {
        val component = factory(scope).builder(Main.Builder::class).build()
        UiTheme {
            Navigator(SetupScreen(component.id))
        }
    }
}

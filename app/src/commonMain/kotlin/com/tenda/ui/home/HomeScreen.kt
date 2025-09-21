package com.tenda.ui.home

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.koin.core.scope.ScopeID

class HomeScreen(val scope: ScopeID) : Screen {
    @Composable
    override fun Content() {
        HomePage()
    }
}

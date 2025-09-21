package com.tenda.ui.setup

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.koin.core.scope.ScopeID

class SetupScreen(val scope: ScopeID) : Screen {
    @Composable
    override fun Content() {
        Text("SetupScreen")
    }
}

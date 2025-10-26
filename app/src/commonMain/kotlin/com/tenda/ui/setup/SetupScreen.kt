package com.tenda.ui.setup

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.tenda.dataBase
import com.tenda.ui.core.extension.factory
import org.koin.core.scope.ScopeID

class SetupScreen(val scope: ScopeID) : Screen {
    @Composable
    override fun Content() {
        val component = factory(scope).builder(Setup.Builder::class).build()
        val viewModel = rememberScreenModel { component.get<SetupViewModel>() }
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = component.getScope(scope).get<SetupEvent>()
        val dataBasePath = dataBase()
        val database = remember { TextFieldState(dataBasePath) }
        val host = remember { TextFieldState("libsql://tandasecure-datainfosec.aws-us-east-1.turso.io") }
        val token = remember { TextFieldState("eyJhbGciOiJFZERTQSIsInR5cCI6IkpXVCJ9.eyJhIjoicnciLCJpYXQiOjE3NDQ5MDE4NDgsImlkIjoiM2NlNmIwNTAtMmQyMC00MzZhLTkxODMtNmE3OTQ5NTJiMjIxIiwicmlkIjoiNDMzMGEwNTItN2EwYy00OWY0LWI2ZTYtMDcwYzYwOWZlNGRmIn0.6VBeB4q_WjrG-99IDKGlzcGU20jOVdMAMcIejmVn6HW5dLaLXClfTfsKfB2LgM7L-pO_KUKs1dtHsW7MFJH1Dg") }
        val isLoading = remember { derivedStateOf {
            state.value is SetupViewModel.State.Loading
        } }
        val isInitialized = remember {
            derivedStateOf {
                state.value is SetupViewModel.State.Initialized
            }
        }
        val error = remember { derivedStateOf {
            (state.value as? SetupViewModel.State.Error?)?.error
        } }
        val enabled = remember {
            derivedStateOf {
                database.text.toString().isNotBlank()
            }
        }
        SetupPage(
            username = database,
            host = host,
            token = token,
            isLoading = isLoading,
            enabled = enabled,
            error = error,
        ) { username, host, token ->
            viewModel.initialize(
                url = host,
                token = token,
                database = username
            )
        }
        LaunchedEffect(isInitialized.value) {
            if (isInitialized.value) {
                event.onSetup(SetupEvent.Event.Initialized)
            }
        }
    }
}

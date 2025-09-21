package com.tenda.ui.setup

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tenda.ui.core.design.DesignButton

@Composable
fun SetupPage(
    username: TextFieldState,
    host: TextFieldState,
    token: TextFieldState,
    isLoading: State<Boolean>,
    enabled: State<Boolean>,
    error: State<Throwable?>,
    onSubmitted: (String, String, String) -> Unit
) {
    val handleSubmit by rememberUpdatedState(onSubmitted)
    Column(modifier = Modifier.fillMaxSize()
        .padding(28.dp)
        .statusBarsPadding()
        .navigationBarsPadding()) {
        Spacer(modifier = Modifier.fillMaxWidth()
            .weight(.5f))
        SetupForm(
            database = username,
            host = host,
            token = token
        )
        Spacer(modifier = Modifier.fillMaxWidth()
            .height(14.dp))
        AnimatedContent(error.value) { value ->
            if (value != null) {
                Column(modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.fillMaxWidth()
                        .height(6.dp))
                    Text(
                        text = value.message?.let {
                            it.ifEmpty {
                                "Unknown error occurred!"
                            }
                        } ?: "Unknown error occurred!",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        Spacer(modifier = Modifier.fillMaxWidth()
            .height(14.dp))
        DesignButton(
            onClick = {
                handleSubmit(
                    username.text.toString(),
                    host.text.toString(),
                    token.text.toString()
                )
            },
            isLoading = isLoading.value,
            enabled = !isLoading.value && enabled.value,
            modifier = Modifier.fillMaxWidth(),
            loading = { Text("Loading...") }
        ) { Text("Setup") }
        Spacer(modifier = Modifier.fillMaxWidth()
            .weight(.5f))
    }
}

package com.tenda.feature.contact.ui.draft

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tenda.ui.core.design.DesignButton
import com.tenda.ui.core.design.DesignTextField

@Composable
fun DraftPage(
    name: TextFieldState,
    isLoading: State<Boolean>,
    enabled: State<Boolean>,
    error: State<Throwable?>,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    val handleOnSave by rememberUpdatedState(onSave)
    Column(modifier = Modifier.padding(16.dp)) {
        DesignTextField(
            state = name,
            hint = "Name",
            enabled = !isLoading.value,
            modifier = Modifier.fillMaxWidth(),
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            textStyle = MaterialTheme.typography.bodyLarge
                .copy(color = MaterialTheme.colorScheme.onBackground)
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
        Row {
            DesignButton(
                isLoading = isLoading.value,
                enabled = !isLoading.value && enabled.value,
                onClick = {
                    handleOnSave(name.text.toString())
                },
                modifier = Modifier.weight(1f)
                    .padding(top = 12.dp)
            ) { Text("Save") }
            DesignButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
                    .padding(top = 12.dp)
            ) { Text("Close") }
        }
    }
}

package com.tenda.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tenda.ui.core.design.DesignButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    isLoading: State<Boolean>,
    error: State<Throwable?>,
    sheetState: SheetState,
    onSynchronised: () -> Unit,
    draft: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val updatedDraft by rememberUpdatedState(draft)
    val updatedContent by rememberUpdatedState(content)
    Column(modifier = Modifier.fillMaxSize()
        .padding(28.dp)
        .statusBarsPadding()
        .navigationBarsPadding()) {
        Row {
            DesignButton(
                isLoading = isLoading.value,
                onClick = onSynchronised,
                modifier = Modifier.weight(1f)
            ) {
                Text("synchronise")
            }
            DesignButton({
                scope.launch {
                    sheetState.show()
                }
            }, modifier = Modifier.weight(1f)) {
                Text("add")
            }
        }
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
        Box(modifier = Modifier.fillMaxWidth()
            .weight(1f)) {
            updatedContent()
        }
        if (sheetState.isVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch { sheetState.hide() }
                },
                sheetState = sheetState,
                properties = ModalBottomSheetProperties(
                    shouldDismissOnBackPress = true
                )
            ) { updatedDraft() }
        }
    }
}

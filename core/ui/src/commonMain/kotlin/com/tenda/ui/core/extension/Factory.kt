package com.tenda.ui.core.extension

import androidx.compose.runtime.Composable
import com.tenda.ui.core.component.UiComponentProvider
import org.koin.compose.getKoin

@Composable
fun factory(scope: String): UiComponentProvider.Factory {
    return get<UiComponentProvider.Factory>(scope)
}

@Composable
inline fun<reified T : Any> get(scope: String): T {
    return getKoin().getScope(scope).get<T>()
}

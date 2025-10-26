package com.tenda

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun dataBase(): String {
    return LocalContext.current.filesDir.path+"/tenda.db"
}
package com.canerture.notes.common

import androidx.compose.runtime.Composable

@Composable
expect fun rememberFilePickerLauncher(
    onResult: (ByteArray?) -> Unit,
): FilePickerLauncher

expect class FilePickerLauncher(
    onLaunch: () -> Unit,
) {
    fun launch()
}

package com.canerture.notes.common

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Composable
actual fun rememberFilePickerLauncher(
    onResult: (ByteArray?) -> Unit,
): FilePickerLauncher {
    val context = LocalContext.current
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val buffer = ByteArrayOutputStream()
                if (inputStream != null) {
                    val bytesRead = ByteArray(1024)
                    var bytes: Int
                    while (inputStream.read(bytesRead).also { bytes = it } != -1) {
                        buffer.write(bytesRead, 0, bytes)
                    }
                    onResult(buffer.toByteArray())
                }
                inputStream?.close()
            }
        }
    )

    return remember {
        FilePickerLauncher(
            onLaunch = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        )
    }
}

actual class FilePickerLauncher actual constructor(
    private val onLaunch: () -> Unit,
) {
    actual fun launch() {
        onLaunch()
    }
}
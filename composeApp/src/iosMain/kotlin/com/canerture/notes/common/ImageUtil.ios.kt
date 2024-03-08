package com.canerture.notes.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.darwin.NSObject
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberFilePickerLauncher(
    onResult: (ByteArray?) -> Unit,
): FilePickerLauncher {
    val delegate = remember {
        object : NSObject(), UIDocumentPickerDelegateProtocol {
            override fun documentPicker(
                controller: UIDocumentPickerViewController,
                didPickDocumentAtURL: NSURL
            ) {
                val data = NSData.dataWithContentsOfURL(didPickDocumentAtURL)
                if (data != null) {
                    onResult(
                        ByteArray(data.length.toInt()).apply {
                            usePinned {
                                memcpy(it.addressOf(0), data.bytes, data.length)
                            }
                        }
                    )
                }
            }

            override fun documentPicker(
                controller: UIDocumentPickerViewController,
                didPickDocumentsAtURLs: List<*>
            ) {
                val url = didPickDocumentsAtURLs.first() as? NSURL ?: return

                val data = NSData.dataWithContentsOfURL(url)
                if (data != null) {
                    onResult(
                        ByteArray(data.length.toInt()).apply {
                            usePinned {
                                memcpy(it.addressOf(0), data.bytes, data.length)
                            }
                        }
                    )
                }
            }
        }
    }

    return remember {
        FilePickerLauncher(
            onLaunch = {
                UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                    createUIDocumentPickerViewController(delegate),
                    true,
                    null
                )
            }
        )
    }
}

private fun createUIDocumentPickerViewController(
    delegate: UIDocumentPickerDelegateProtocol,
): UIDocumentPickerViewController {

    val pickerController = UIDocumentPickerViewController(
        forOpeningContentTypes = listOf(UTTypeImage),
        asCopy = true,
    )
    pickerController.delegate = delegate
    return pickerController
}

actual class FilePickerLauncher actual constructor(
    private val onLaunch: () -> Unit,
) {
    actual fun launch() {
        onLaunch()
    }
}
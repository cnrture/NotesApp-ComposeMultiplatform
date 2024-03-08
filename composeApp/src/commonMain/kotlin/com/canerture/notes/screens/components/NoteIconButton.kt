package com.canerture.notes.screens.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.canerture.notes.theme.orange
import com.canerture.notes.theme.white

@Composable
fun NoteIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors(containerColor = orange),
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = white
        )
    }
}
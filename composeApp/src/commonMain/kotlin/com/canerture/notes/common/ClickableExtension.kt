package com.canerture.notes.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier

fun Modifier.clickableWithoutRipple(onClick: () -> Unit) = this then Modifier.clickable(
    onClick = onClick,
    indication = null,
    interactionSource = MutableInteractionSource()
)
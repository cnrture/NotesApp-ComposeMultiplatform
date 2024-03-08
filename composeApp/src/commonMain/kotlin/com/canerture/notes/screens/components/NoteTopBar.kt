package com.canerture.notes.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canerture.notes.theme.orange
import com.canerture.notes.theme.white

@Composable
fun NoteTopBar(
    title: String,
    isBackIconVisible: Boolean = false,
    onBackClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = orange),
        contentAlignment = Alignment.Center
    ) {
        if (isBackIconVisible) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = { onBackClick?.invoke() },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = white
                )
            }
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            text = title,
            color = white,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Black,
            fontSize = 20.sp
        )
    }
}
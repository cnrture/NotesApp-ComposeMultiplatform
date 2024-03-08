package com.canerture.notes.screens.notes.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canerture.notes.common.clickableWithoutRipple
import com.canerture.notes.domain.model.NoteUI
import com.canerture.notes.screens.components.NoteIconButton
import com.canerture.notes.theme.orange
import com.canerture.notes.theme.orangeLight

@Composable
fun NoteItem(
    note: NoteUI,
    onNoteClick: (id: Int) -> Unit,
    onDeleteClick: (id: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickableWithoutRipple {
                onNoteClick(note.id)
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = orangeLight
        ),
        border = BorderStroke(1.dp, orange)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = note.title,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
            )

            NoteIconButton(
                icon = Icons.Default.Delete,
                onClick = {
                    onDeleteClick(note.id)
                }
            )
        }
    }
}
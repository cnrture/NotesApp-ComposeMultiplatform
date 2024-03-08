package com.canerture.notes.screens.notedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.canerture.notes.MR
import com.canerture.notes.domain.model.NoteUI
import com.canerture.notes.screens.components.NoteProgressBar
import com.canerture.notes.screens.components.NoteTopBar
import com.canerture.notes.screens.notedetail.components.NoteTextField
import com.canerture.notes.theme.orange
import dev.icerock.moko.resources.compose.stringResource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

data class NoteDetailScreen(val id: Int) : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val screenModel: NoteDetailScreenModel = getScreenModel()

        screenModel.onEvent(NoteDetailEvent.GetNoteDetail(id))

        val state by screenModel.state.collectAsState()
        val effect by screenModel.effect.collectAsState(NoteDetailEffect.None)

        when (effect) {
            NoteDetailEffect.None -> Unit
            is NoteDetailEffect.NavigateBack -> {
                navigator.pop()
            }

            is NoteDetailEffect.ShowError -> {}
        }

        Scaffold(
            topBar = {
                NoteTopBar(
                    title = "Note Detail",
                    isBackIconVisible = true,
                    onBackClick = { navigator.pop() }
                )
            },
        ) { paddingValues ->
            state.note?.let {
                DetailContent(
                    modifier = Modifier.padding(paddingValues),
                    note = it,
                    onAction = { screenModel.onEvent(it) }
                )
            }
            if (state.isLoading) {
                NoteProgressBar()
            }
        }
    }

    @Composable
    private fun DetailContent(
        modifier: Modifier = Modifier,
        note: NoteUI,
        onAction: (NoteDetailEvent) -> Unit
    ) {
        var title by remember { mutableStateOf(note.title) }
        var text by remember { mutableStateOf(note.text) }

        var isEdit by remember { mutableStateOf(false) }

        Column(
            modifier = modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (note.imageUrl.isNotEmpty()) {
                    KamelImage(
                        modifier = Modifier.size(200.dp),
                        resource = asyncPainterResource(data = note.imageUrl),
                        contentDescription = null,
                    )
                }

                if (isEdit) {
                    NoteTextField(
                        value = title,
                        onValueChange = { title = it },
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    NoteTextField(
                        value = text,
                        onValueChange = { text = it },
                    )
                } else {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = note.title,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = note.text,
                        color = Color.Black,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                onClick = {
                    isEdit = !isEdit
                    if (!isEdit) {
                        onAction(NoteDetailEvent.EditNote(note.id, title, text))
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = orange
                )
            ) {
                Text(
                    text = stringResource(if (isEdit) MR.strings.save else MR.strings.edit),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
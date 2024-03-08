package com.canerture.notes.screens.notes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.canerture.notes.MR
import com.canerture.notes.common.rememberFilePickerLauncher
import com.canerture.notes.common.toImageBitmap
import com.canerture.notes.domain.model.NoteUI
import com.canerture.notes.screens.components.NoteIconButton
import com.canerture.notes.screens.components.NoteProgressBar
import com.canerture.notes.screens.components.NoteTopBar
import com.canerture.notes.screens.notedetail.NoteDetailScreen
import com.canerture.notes.screens.notes.components.InputField
import com.canerture.notes.screens.notes.components.NoteItem
import com.canerture.notes.theme.orange
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class NotesScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val screenModel: NotesScreenModel = getScreenModel()

        val state by screenModel.state.collectAsState()
        val effect by screenModel.effect.collectAsState(NotesEffect.None)

        val sheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden
        )
        val scope = rememberCoroutineScope()

        when (effect) {
            NotesEffect.None -> Unit

            is NotesEffect.GoToNoteDetail -> {
                navigator.push(NoteDetailScreen((effect as NotesEffect.GoToNoteDetail).id))
            }

            is NotesEffect.ShowError -> {}
        }

        AddBottomSheet(
            sheetState = sheetState,
            onAddClick = { title, text, image ->
                screenModel.onEvent(NotesEvent.AddNoteClick(title, text, image))
                scope.launch {
                    sheetState.hide()
                }
            },
            content = {
                Scaffold(
                    topBar = {
                        NoteTopBar(title = stringResource(MR.strings.my_notes))
                    },
                    floatingActionButton = {
                        if (!state.isLoading) {
                            NoteIconButton(
                                icon = Icons.Default.Add,
                                onClick = {
                                    scope.launch {
                                        sheetState.show()
                                    }
                                }
                            )
                        }
                    }
                ) { paddingValues ->
                    if (state.noteList.isNotEmpty()) {
                        NoteList(
                            modifier = Modifier.padding(paddingValues),
                            noteList = state.noteList,
                            onAction = screenModel::onEvent
                        )
                    }
                    if (state.isLoading) {
                        NoteProgressBar()
                    }
                }
            }
        )
    }

    @Composable
    private fun NoteList(
        modifier: Modifier = Modifier,
        noteList: List<NoteUI>,
        onAction: (NotesEvent) -> Unit
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp)
        ) {
            items(noteList) { note ->
                NoteItem(
                    note = note,
                    onNoteClick = {
                        onAction(NotesEvent.NoteClick(it))
                    },
                    onDeleteClick = {
                        onAction(NotesEvent.DeleteNoteClick(it))
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalEncodingApi::class)
    @Composable
    private fun AddBottomSheet(
        sheetState: ModalBottomSheetState,
        onAddClick: (String, String, String?) -> Unit,
        content: @Composable () -> Unit,
    ) {
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                var title by rememberSaveable { mutableStateOf("") }
                var text by rememberSaveable { mutableStateOf("") }
                var image by rememberSaveable { mutableStateOf<String?>(null) }
                var imageBitmap by rememberSaveable { mutableStateOf<ImageBitmap?>(null) }
                val keyboard = LocalFocusManager.current

                val pickImageLauncher = rememberFilePickerLauncher(
                    onResult = {
                        it?.let {
                            image = Base64.encode(it, 0, it.size)
                            imageBitmap = it.toImageBitmap()
                            keyboard.clearFocus()
                        }
                    }
                )

                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    InputField(
                        input = title,
                        label = stringResource(MR.strings.title),
                        onInputChange = { title = it }
                    )

                    InputField(
                        input = text,
                        label = stringResource(MR.strings.text),
                        onInputChange = { text = it }
                    )

                    imageBitmap?.let {
                        Image(
                            bitmap = it,
                            contentDescription = stringResource(MR.strings.image),
                            modifier = Modifier
                                .padding(8.dp)
                                .size(100.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        border = BorderStroke(2.dp, orange),
                        onClick = {
                            pickImageLauncher.launch()
                        }
                    ) {
                        Text(stringResource(MR.strings.add_image))
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = orange),
                        onClick = {
                            onAddClick(title, text, image)
                        }
                    ) {
                        Text(stringResource(MR.strings.add_note))
                    }
                }
            },
            content = content
        )
    }
}
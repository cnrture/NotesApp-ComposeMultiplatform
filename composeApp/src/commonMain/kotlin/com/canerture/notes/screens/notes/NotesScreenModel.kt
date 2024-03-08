package com.canerture.notes.screens.notes

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.canerture.notes.domain.model.NoteUI
import com.canerture.notes.domain.usecase.AddNoteUseCase
import com.canerture.notes.domain.usecase.DeleteNoteUseCase
import com.canerture.notes.domain.usecase.GetNotesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesScreenModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : StateScreenModel<NotesState>(NotesState()) {

    private val _effect = MutableSharedFlow<NotesEffect>()
    val effect: SharedFlow<NotesEffect> = _effect.asSharedFlow()

    init {
        getNotes()
    }

    fun onEvent(event: NotesEvent) = screenModelScope.launch {
        when (event) {
            is NotesEvent.AddNoteClick -> {
                addNote(event.title, event.text, event.image)
            }

            is NotesEvent.NoteClick -> setEffect(NotesEffect.GoToNoteDetail(event.id))

            is NotesEvent.DeleteNoteClick -> deleteNote(event.id)
        }
    }

    private fun getNotes() = screenModelScope.launch {
        setState { copy(isLoading = true) }

        getNotesUseCase().onSuccess {
            setState { copy(noteList = it) }
        }.onFailure {
            setEffect(NotesEffect.ShowError(it.message.orEmpty()))
        }
        setState { copy(isLoading = false) }
    }

    private fun addNote(title: String, text: String, image: String?) = screenModelScope.launch {
        setState { copy(isLoading = true) }

        addNoteUseCase(title, text, image).onSuccess {
            getNotes()
        }.onFailure {
            setEffect(NotesEffect.ShowError(it.message.orEmpty()))
        }
        setState { copy(isLoading = false) }
    }

    private fun deleteNote(id: Int) = screenModelScope.launch {
        setState { copy(isLoading = true) }

        deleteNoteUseCase(id).onSuccess {
            getNotes()
        }.onFailure {
            setEffect(NotesEffect.ShowError(it.message.orEmpty()))
        }
        setState { copy(isLoading = false) }
    }

    private fun setState(reducer: NotesState.() -> NotesState) {
        mutableState.update {
            reducer(it)
        }
    }

    private fun setEffect(effect: NotesEffect) {
        screenModelScope.launch {
            _effect.emit(effect)
        }
    }
}

data class NotesState(
    val isLoading: Boolean = false,
    val noteList: List<NoteUI> = emptyList()
)

sealed interface NotesEvent {
    data class AddNoteClick(val title: String, val text: String, val image: String?) : NotesEvent
    data class NoteClick(val id: Int) : NotesEvent
    data class DeleteNoteClick(val id: Int) : NotesEvent
}

sealed interface NotesEffect {
    data object None : NotesEffect
    data class ShowError(val message: String) : NotesEffect
    data class GoToNoteDetail(val id: Int) : NotesEffect
}
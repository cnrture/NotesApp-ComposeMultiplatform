package com.canerture.notes.screens.notedetail

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.canerture.notes.domain.model.NoteUI
import com.canerture.notes.domain.usecase.DeleteNoteUseCase
import com.canerture.notes.domain.usecase.EditNoteUseCase
import com.canerture.notes.domain.usecase.GetNoteDetailUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteDetailScreenModel(
    private val getNoteDetailUseCase: GetNoteDetailUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val editNoteUseCase: EditNoteUseCase
) : StateScreenModel<NoteDetailState>(NoteDetailState()) {

    private val _effect = MutableSharedFlow<NoteDetailEffect>()
    val effect: SharedFlow<NoteDetailEffect> = _effect.asSharedFlow()

    fun onEvent(event: NoteDetailEvent) = screenModelScope.launch {
        when (event) {
            is NoteDetailEvent.GetNoteDetail -> getNoteDetail(event.id)
            is NoteDetailEvent.DeleteNote -> deleteNote(event.id)
            is NoteDetailEvent.EditNote -> editNote(event.id, event.title, event.text)
        }
    }

    private fun getNoteDetail(id: Int) = screenModelScope.launch {
        setState { copy(isLoading = true) }

        getNoteDetailUseCase(id).onSuccess {
            setState { copy(note = it) }
        }.onFailure {
            setEffect(NoteDetailEffect.ShowError(it.message.orEmpty()))
        }
        setState { copy(isLoading = false) }
    }

    private fun deleteNote(id: Int) = screenModelScope.launch {
        setState { copy(isLoading = true) }

        deleteNoteUseCase(id).onSuccess {
            getNoteDetail(id)
        }.onFailure {
            setState { copy(isLoading = false) }
        }
    }

    private fun editNote(id: Int, title: String, text: String) = screenModelScope.launch {
        setState { copy(isLoading = true) }

        editNoteUseCase(id, title, text).onSuccess {
            getNoteDetail(id)
        }.onFailure {
            setState { copy(isLoading = false) }
        }
    }

    private fun setState(reducer: NoteDetailState.() -> NoteDetailState) {
        mutableState.update {
            reducer(it)
        }
    }

    private fun setEffect(effect: NoteDetailEffect) {
        screenModelScope.launch {
            _effect.emit(effect)
        }
    }
}

data class NoteDetailState(
    val isLoading: Boolean = false,
    val note: NoteUI? = null,
)

sealed interface NoteDetailEvent {
    data class GetNoteDetail(val id: Int) : NoteDetailEvent
    data class DeleteNote(val id: Int) : NoteDetailEvent
    data class EditNote(val id: Int, val title: String, val text: String) : NoteDetailEvent
}

sealed interface NoteDetailEffect {
    data object None : NoteDetailEffect
    data object NavigateBack : NoteDetailEffect
    data class ShowError(val message: String) : NoteDetailEffect
}
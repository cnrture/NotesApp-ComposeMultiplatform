package com.canerture.notes.domain.usecase

import com.canerture.notes.data.model.response.Note
import com.canerture.notes.domain.model.NoteUI
import com.canerture.notes.domain.repository.NotesRepository

class GetNotesUseCase(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(): Result<List<NoteUI>> {
        return notesRepository.getNotes().map {
            it.data.mapToNoteUIModel()
        }
    }

    private fun List<Note>?.mapToNoteUIModel() = this?.map {
        NoteUI(
            id = it.id ?: 0,
            title = it.title.orEmpty(),
            text = it.text.orEmpty(),
            imageUrl = it.imageUrl.orEmpty()
        )
    }.orEmpty()
}
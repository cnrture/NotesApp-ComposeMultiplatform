package com.canerture.notes.domain.usecase

import com.canerture.notes.data.model.response.Note
import com.canerture.notes.domain.model.NoteUI
import com.canerture.notes.domain.repository.NotesRepository

class GetNoteDetailUseCase(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(id: Int): Result<NoteUI> {
        return notesRepository.getNoteDetail(id).map {
            it.data.mapToNoteUI()
        }
    }

    private fun Note?.mapToNoteUI() = NoteUI(
        id = this?.id ?: 0,
        title = this?.title.orEmpty(),
        text = this?.text.orEmpty(),
        imageUrl = this?.imageUrl.orEmpty()
    )
}
package com.canerture.notes.domain.usecase

import com.canerture.notes.data.model.request.EditNoteRequest
import com.canerture.notes.domain.repository.NotesRepository

class EditNoteUseCase(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(id: Int, title: String, text: String): Result<Unit> {
        return notesRepository.editNote(EditNoteRequest(id, title, text)).map {
            Result.success(Unit)
        }
    }
}
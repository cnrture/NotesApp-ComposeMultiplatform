package com.canerture.notes.domain.usecase

import com.canerture.notes.data.model.request.DeleteNoteRequest
import com.canerture.notes.domain.repository.NotesRepository

class DeleteNoteUseCase(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return notesRepository.deleteNote(DeleteNoteRequest(id)).map {
            Result.success(Unit)
        }
    }
}
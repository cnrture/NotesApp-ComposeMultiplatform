package com.canerture.notes.domain.usecase

import com.canerture.notes.data.model.request.AddNoteRequest
import com.canerture.notes.domain.repository.NotesRepository

class AddNoteUseCase(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(title: String, text: String, image: String?): Result<Unit> {
        return notesRepository.addNote(AddNoteRequest(title, text, image)).map {
            Result.success(Unit)
        }
    }
}
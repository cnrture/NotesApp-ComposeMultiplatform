package com.canerture.notes.data.repository

import com.canerture.notes.data.model.request.AddNoteRequest
import com.canerture.notes.data.model.request.DeleteNoteRequest
import com.canerture.notes.data.model.request.EditNoteRequest
import com.canerture.notes.data.model.response.BaseResponse
import com.canerture.notes.data.model.response.GetNoteDetailResponse
import com.canerture.notes.data.model.response.GetNotesResponse
import com.canerture.notes.data.source.NotesService
import com.canerture.notes.domain.repository.NotesRepository

class NotesRepositoryImpl(
    private val notesService: NotesService,
) : NotesRepository {

    override suspend fun addNote(addNoteRequest: AddNoteRequest): Result<BaseResponse> {
        return safeApiCall {
            notesService.addNote(addNoteRequest)
        }
    }

    override suspend fun getNotes(): Result<GetNotesResponse> {
        return safeApiCall {
            notesService.getNotes()
        }
    }

    override suspend fun getNoteDetail(id: Int): Result<GetNoteDetailResponse> {
        return safeApiCall { notesService.getNoteDetail(id) }
    }

    override suspend fun deleteNote(deleteNoteRequest: DeleteNoteRequest): Result<BaseResponse> {
        return safeApiCall {
            notesService.deleteNote(deleteNoteRequest)
        }
    }

    override suspend fun editNote(editNoteRequest: EditNoteRequest): Result<BaseResponse> {
        return safeApiCall {
            notesService.editNote(editNoteRequest)
        }
    }

    private suspend fun <T : BaseResponse> safeApiCall(call: suspend () -> T): Result<T> {
        return try {
            val response = call()
            if (response.status == 200) {
                Result.success(response)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

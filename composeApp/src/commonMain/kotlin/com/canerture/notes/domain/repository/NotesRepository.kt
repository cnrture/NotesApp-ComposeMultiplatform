package com.canerture.notes.domain.repository

import com.canerture.notes.data.model.request.AddNoteRequest
import com.canerture.notes.data.model.request.DeleteNoteRequest
import com.canerture.notes.data.model.request.EditNoteRequest
import com.canerture.notes.data.model.response.BaseResponse
import com.canerture.notes.data.model.response.GetNoteDetailResponse
import com.canerture.notes.data.model.response.GetNotesResponse

interface NotesRepository {

    suspend fun addNote(addNoteRequest: AddNoteRequest): Result<BaseResponse>

    suspend fun getNotes(): Result<GetNotesResponse>

    suspend fun getNoteDetail(id: Int): Result<GetNoteDetailResponse>

    suspend fun deleteNote(deleteNoteRequest: DeleteNoteRequest): Result<BaseResponse>

    suspend fun editNote(editNoteRequest: EditNoteRequest): Result<BaseResponse>
}

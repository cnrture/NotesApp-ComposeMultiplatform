package com.canerture.notes.data.source

import com.canerture.notes.BuildKonfig
import com.canerture.notes.data.model.request.AddNoteRequest
import com.canerture.notes.data.model.request.DeleteNoteRequest
import com.canerture.notes.data.model.request.EditNoteRequest
import com.canerture.notes.data.model.response.BaseResponse
import com.canerture.notes.data.model.response.GetNoteDetailResponse
import com.canerture.notes.data.model.response.GetNotesResponse
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class NotesService : KtorApi() {

    private val baseUrl = BuildKonfig.BASE_URL
    private val notes = baseUrl.plus(NOTES)

    suspend fun addNote(addNoteRequest: AddNoteRequest): BaseResponse {
        return client.post(notes) {
            setBody(addNoteRequest)
        }.body<BaseResponse>()
    }

    suspend fun getNotes(): GetNotesResponse {
        return client.get(notes).body<GetNotesResponse>()
    }

    suspend fun getNoteDetail(id: Int): GetNoteDetailResponse {
        return client.get(notes) {
            parameter("id", id)
        }.body<GetNoteDetailResponse>()
    }

    suspend fun deleteNote(deleteNoteRequest: DeleteNoteRequest): BaseResponse {
        return client.delete(notes) {
            setBody(deleteNoteRequest)
        }.body<BaseResponse>()
    }

    suspend fun editNote(editNoteRequest: EditNoteRequest): BaseResponse {
        return client.put(notes) {
            setBody(editNoteRequest)
        }.body<BaseResponse>()
    }

    companion object {
        private const val NOTES = "notes"
    }
}

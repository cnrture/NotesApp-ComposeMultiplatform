package com.canerture.notes.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class GetNotesResponse(
    val data: List<Note>?
) : BaseResponse()

package com.canerture.notes.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class GetNoteDetailResponse(
    val data: Note?
) : BaseResponse()
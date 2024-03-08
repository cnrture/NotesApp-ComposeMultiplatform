package com.canerture.notes.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteNoteRequest(
    val id: Int
)
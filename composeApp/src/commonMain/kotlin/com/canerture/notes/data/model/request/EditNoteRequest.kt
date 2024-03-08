package com.canerture.notes.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class EditNoteRequest(
    val id: Int,
    val title: String,
    val text: String,
)
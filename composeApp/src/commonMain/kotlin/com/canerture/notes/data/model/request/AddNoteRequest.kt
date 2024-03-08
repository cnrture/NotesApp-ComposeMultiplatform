package com.canerture.notes.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class AddNoteRequest(
    val title: String,
    val text: String,
    val image: String? = null
)
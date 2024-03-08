package com.canerture.notes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NoteUI(
    val id: Int,
    val title: String,
    val text: String,
    val imageUrl: String,
)
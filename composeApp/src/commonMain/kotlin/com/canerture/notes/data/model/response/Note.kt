package com.canerture.notes.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Int?,
    val title: String?,
    val text: String?,
    val imageUrl: String?
)

package com.canerture.notes

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.canerture.notes.screens.notes.NotesScreen

@Composable
fun App() {
    MaterialTheme {
        Navigator(NotesScreen())
    }
}

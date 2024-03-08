package com.canerture.notes.di

import com.canerture.notes.data.repository.NotesRepositoryImpl
import com.canerture.notes.data.source.NotesService
import com.canerture.notes.domain.repository.NotesRepository
import com.canerture.notes.domain.usecase.AddNoteUseCase
import com.canerture.notes.domain.usecase.DeleteNoteUseCase
import com.canerture.notes.domain.usecase.EditNoteUseCase
import com.canerture.notes.domain.usecase.GetNoteDetailUseCase
import com.canerture.notes.domain.usecase.GetNotesUseCase
import com.canerture.notes.screens.notedetail.NoteDetailScreenModel
import com.canerture.notes.screens.notes.NotesScreenModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    single<NotesService> { NotesService() }
    single<NotesRepository> { NotesRepositoryImpl(get()) }
}

val useCaseModule = module {
    factoryOf(::AddNoteUseCase)
    factoryOf(::GetNotesUseCase)
    factoryOf(::DeleteNoteUseCase)
    factoryOf(::EditNoteUseCase)
    factoryOf(::GetNoteDetailUseCase)
}

val screenModelsModule = module {
    factoryOf(::NotesScreenModel)
    factoryOf(::NoteDetailScreenModel)
}

fun initKoin() {
    startKoin { modules(dataModule, useCaseModule, screenModelsModule) }
}

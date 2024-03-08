package com.canerture.notes

import android.app.Application
import com.canerture.notes.di.initKoin

class NotesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}

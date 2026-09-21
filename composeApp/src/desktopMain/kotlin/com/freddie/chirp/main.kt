package com.freddie.chirp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.freddie.chirp.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Chirp",
        ) {
            App()
        }
    }
}
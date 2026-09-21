package com.freddie.chirp

import androidx.compose.runtime.Composable
import com.freddie.auth.presentation.register.RegisterRoot
import com.freddie.core.designsystem.theme.ChirpTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    ChirpTheme {
        RegisterRoot()
    }
}
package com.freddie.chirp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.freddie.chirp.navigation.NavigationRoot
import com.freddie.core.designsystem.theme.ChirpTheme

@Composable
@Preview
fun App() {
    ChirpTheme {
        NavigationRoot()
    }
}
package com.freddie.auth.presentation.register

interface RegisterEvent {
    data class Success(val email: String) : RegisterEvent
}
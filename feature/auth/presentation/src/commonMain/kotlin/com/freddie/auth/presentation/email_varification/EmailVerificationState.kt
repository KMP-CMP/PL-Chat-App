package com.freddie.auth.presentation.email_varification

data class EmailVerificationState(
    val isVerifying: Boolean = false,
    val isVerified: Boolean = false
)
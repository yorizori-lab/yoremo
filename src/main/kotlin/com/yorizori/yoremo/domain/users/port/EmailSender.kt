package com.yorizori.yoremo.domain.users.port

interface EmailSender {
    fun sendVerificationEmail(email: String, token: String, name: String)
    fun sendPasswordResetEmail(email: String, token: String, name: String)
    fun sendWelcomeEmail(email: String, name: String)
}

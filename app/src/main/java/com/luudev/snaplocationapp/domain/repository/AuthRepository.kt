package com.luudev.snaplocationapp.domain.repository

import com.luudev.snaplocationapp.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, pass: String): Result<User>
    suspend fun register(email: String,pass: String): Result<User>
    fun getCurrentUser(): User?
    fun logout()
}
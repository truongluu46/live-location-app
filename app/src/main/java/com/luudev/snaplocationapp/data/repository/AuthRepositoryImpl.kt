package com.luudev.snaplocationapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.luudev.snaplocationapp.domain.model.User
import com.luudev.snaplocationapp.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
): AuthRepository {
    override suspend fun login(email: String, pass: String): Result<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                Result.success(User(uid = firebaseUser.uid, email = firebaseUser.email ?: " "))
            } else {
                Result.failure(Exception("Đăng nhập thất bại"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, pass: String): Result<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                Result.success(User(uid = firebaseUser.uid, email = firebaseUser.email ?: ""))
            } else {
                Result.failure(Exception("Đăng ký thất bại"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser
        return firebaseUser?.let { User(uid = it.uid, email = it.email ?: "") }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }


}
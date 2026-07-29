package com.luudev.snaplocationapp.domain.model

data class User(
    val uid: String,
    val email: String,
    //val name: String,
    val token: String? = null
)

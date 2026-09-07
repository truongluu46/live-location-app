package com.luudev.snaplocationapp.ui.auth

import android.os.Message
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luudev.snaplocationapp.data.repository.AuthRepositoryImpl
import com.luudev.snaplocationapp.domain.repository.AuthRepository
import kotlinx.coroutines.launch


sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading: AuthUiState
    data class Success(val email: String) : AuthUiState
    data class Error (val message: String) : AuthUiState
}

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    var uiState by mutableStateOf<AuthUiState>(AuthUiState.Idle)
        private set

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            uiState = AuthUiState.Error("Email và mật khẩu không được để trống")
            return
        }

        viewModelScope.launch {
            uiState = AuthUiState.Loading
            var result = authRepository.login(email, pass)
            result.onSuccess { user ->
                uiState = AuthUiState.Success(user.email)
            }.onFailure { error ->
                uiState = AuthUiState.Error(error.localizedMessage ?: "Đăng nhập thất bại")
            }

        }
    }

    fun register(email: String, pass: String) {
        if(email.isBlank() || pass.isBlank()) {
            uiState = AuthUiState.Error("Email và mật khẩu không được để trống")
            return
        }

        viewModelScope.launch {
            uiState = AuthUiState.Loading
            val result = authRepository.register(email, pass)
            result.onSuccess { user ->
                uiState = AuthUiState.Success(user.email)
            }.onFailure { error ->
                uiState = AuthUiState.Error(error.localizedMessage ?: "Đăng ký thất bại")
            }
        }
    }

}
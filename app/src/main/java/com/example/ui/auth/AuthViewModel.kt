package com.example.ui.auth

import androidx.lifecycle.ViewModel
import com.example.data.model.AuthType
import com.example.data.model.User
import com.example.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AuthTab {
    EMAIL,
    PHONE
}

enum class PhoneStep {
    ENTER_PHONE,
    ENTER_OTP
}

data class AuthUiState(
    val activeTab: AuthTab = AuthTab.EMAIL,
    val emailInput: String = "",
    val passwordInput: String = "",
    val nameInput: String = "",
    val isSignUpMode: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,

    val phoneInput: String = "",
    val otpInput: String = "",
    val phoneStep: PhoneStep = PhoneStep.ENTER_PHONE,
    val phoneError: String? = null,
    val otpError: String? = null,

    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false
)

class AuthViewModel(private val repository: MusicRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun setTab(tab: AuthTab) {
        _uiState.value = _uiState.value.copy(
            activeTab = tab,
            emailError = null,
            passwordError = null,
            phoneError = null,
            otpError = null
        )
    }

    fun toggleSignUpMode() {
        _uiState.value = _uiState.value.copy(
            isSignUpMode = !_uiState.value.isSignUpMode,
            emailError = null,
            passwordError = null
        )
    }

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(emailInput = email, emailError = null)
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(passwordInput = password, passwordError = null)
    }

    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(nameInput = name)
    }

    fun onPhoneChanged(phone: String) {
        _uiState.value = _uiState.value.copy(phoneInput = phone.filter { it.isDigit() }, phoneError = null)
    }

    fun onOtpChanged(otp: String) {
        _uiState.value = _uiState.value.copy(otpInput = otp.filter { it.isDigit() }, otpError = null)
    }

    fun submitEmailAuth() {
        val email = _uiState.value.emailInput.trim()
        val password = _uiState.value.passwordInput

        var valid = true
        var eError: String? = null
        var pError: String? = null

        if (email.isBlank() || !email.contains("@") || !email.contains(".")) {
            eError = "Please enter a valid email address."
            valid = false
        }

        if (password.length < 6) {
            pError = "Password must be at least 6 characters long."
            valid = false
        }

        if (!valid) {
            _uiState.value = _uiState.value.copy(emailError = eError, passwordError = pError)
            return
        }

        // Authenticate user
        val name = if (_uiState.value.nameInput.isNotBlank()) _uiState.value.nameInput else email.substringBefore("@").replaceFirstChar { it.uppercase() }
        val user = User(
            name = name,
            email = email,
            authType = AuthType.EMAIL,
            isVipMember = true
        )
        repository.setUser(user)
        _uiState.value = _uiState.value.copy(isAuthenticated = true)
    }

    fun sendOtp() {
        val phone = _uiState.value.phoneInput.trim()
        if (phone.length != 10) {
            _uiState.value = _uiState.value.copy(phoneError = "Enter a valid 10-digit mobile number.")
            return
        }

        _uiState.value = _uiState.value.copy(
            phoneStep = PhoneStep.ENTER_OTP,
            phoneError = null
        )
    }

    fun verifyOtp() {
        val otp = _uiState.value.otpInput.trim()
        if (otp.length < 4) {
            _uiState.value = _uiState.value.copy(otpError = "Enter a valid 4-digit OTP code (e.g. 1234).")
            return
        }

        val phone = "+91 " + _uiState.value.phoneInput
        val user = User(
            name = "Music Lover",
            phone = phone,
            authType = AuthType.PHONE,
            isVipMember = true
        )
        repository.setUser(user)
        _uiState.value = _uiState.value.copy(isAuthenticated = true)
    }

    fun loginAsDemoUser() {
        val user = User(
            name = "Alex Rivera",
            email = "alex.rivera@musicstream.com",
            phone = "+91 9876543210",
            authType = AuthType.EMAIL,
            isVipMember = true
        )
        repository.setUser(user)
        _uiState.value = _uiState.value.copy(isAuthenticated = true)
    }

    fun logout() {
        repository.setUser(null)
        _uiState.value = AuthUiState()
    }
}

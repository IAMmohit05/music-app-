package com.example.data.model

data class User(
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val authType: AuthType = AuthType.GUEST,
    val isVipMember: Boolean = true
)

enum class AuthType {
    EMAIL,
    PHONE,
    GUEST
}

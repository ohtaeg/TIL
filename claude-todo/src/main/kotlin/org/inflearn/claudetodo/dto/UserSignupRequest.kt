package org.inflearn.claudetodo.dto

data class UserSignupRequest(
    val email: String,
    val password: String,
    val nickname: String
)

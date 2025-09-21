package org.inflearn.claudetodo.service

import org.inflearn.claudetodo.dto.UserLoginRequest
import org.inflearn.claudetodo.dto.UserSignupRequest
import org.inflearn.claudetodo.entity.User
import org.inflearn.claudetodo.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun signup(request: UserSignupRequest): User {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("이미 존재하는 이메일입니다.")
        }

        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            nickname = request.nickname
        )

        return userRepository.save(user)
    }

    fun login(request: UserLoginRequest): User {
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("존재하지 않는 사용자입니다.")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        return user
    }

    fun findById(id: Long): User {
        return userRepository.findById(id).orElseThrow {
            IllegalArgumentException("존재하지 않는 사용자입니다.")
        }
    }
}

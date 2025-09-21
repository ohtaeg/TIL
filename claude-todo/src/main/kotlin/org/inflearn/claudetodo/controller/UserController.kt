package org.inflearn.claudetodo.controller

import jakarta.servlet.http.HttpSession
import org.inflearn.claudetodo.dto.UserLoginRequest
import org.inflearn.claudetodo.dto.UserSignupRequest
import org.inflearn.claudetodo.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class UserController(
    private val userService: UserService
) {

    @GetMapping("/signup")
    fun signupForm(model: Model): String {
        model.addAttribute("userSignupRequest", UserSignupRequest("", "", ""))
        return "signup"
    }

    @PostMapping("/signup")
    fun signup(
        @ModelAttribute userSignupRequest: UserSignupRequest,
        model: Model
    ): String {
        return try {
            userService.signup(userSignupRequest)
            "redirect:/login?signup=success"
        } catch (e: IllegalArgumentException) {
            model.addAttribute("error", e.message)
            model.addAttribute("userSignupRequest", userSignupRequest)
            "signup"
        }
    }

    @GetMapping("/login")
    fun loginForm(
        model: Model,
        @RequestParam(required = false) signup: String?,
        @RequestParam(required = false) error: String?
    ): String {
        model.addAttribute("userLoginRequest", UserLoginRequest("", ""))
        signup?.let { model.addAttribute("signupSuccess", "회원가입이 완료되었습니다. 로그인해주세요.") }
        error?.let { model.addAttribute("error", "이메일 또는 비밀번호가 일치하지 않습니다.") }
        return "login"
    }

    @PostMapping("/login")
    fun login(
        @ModelAttribute userLoginRequest: UserLoginRequest,
        session: HttpSession
    ): String {
        return try {
            val user = userService.login(userLoginRequest)
            session.setAttribute("userId", user.id)
            session.setAttribute("userNickname", user.nickname)
            "redirect:/todos"
        } catch (e: IllegalArgumentException) {
            "redirect:/login?error=true"
        }
    }

    @PostMapping("/logout")
    fun logout(session: HttpSession): String {
        session.invalidate()
        return "redirect:/login"
    }
}

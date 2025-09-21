package org.inflearn.claudetodo.controller

import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {

    @GetMapping("/")
    fun home(session: HttpSession): String {
        val userId = session.getAttribute("userId")
        return if (userId != null) {
            "redirect:/todos"
        } else {
            "redirect:/login"
        }
    }
}

package org.inflearn.claudetodo.controller

import jakarta.servlet.http.HttpSession
import org.inflearn.claudetodo.dto.TodoCreateRequest
import org.inflearn.claudetodo.dto.TodoUpdateRequest
import org.inflearn.claudetodo.service.TodoService
import org.inflearn.claudetodo.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/todos")
class TodoWebController(
    private val todoService: TodoService,
    private val userService: UserService
) {

    @GetMapping
    fun todoList(model: Model, session: HttpSession): String {
        val userId = session.getAttribute("userId") as? Long
            ?: return "redirect:/login"

        val user = userService.findById(userId)
        val todos = todoService.getAllTodos(user)

        model.addAttribute("todos", todos)
        model.addAttribute("userNickname", session.getAttribute("userNickname"))
        return "todos/list"
    }

    @GetMapping("/create")
    fun createForm(model: Model, session: HttpSession): String {
        val userId = session.getAttribute("userId") as? Long
            ?: return "redirect:/login"

        model.addAttribute("todoCreateRequest", TodoCreateRequest("", null))
        model.addAttribute("userNickname", session.getAttribute("userNickname"))
        return "todos/create"
    }

    @PostMapping("/create")
    fun createTodo(
        @ModelAttribute todoCreateRequest: TodoCreateRequest,
        session: HttpSession
    ): String {
        val userId = session.getAttribute("userId") as? Long
            ?: return "redirect:/login"

        val user = userService.findById(userId)
        todoService.createTodo(todoCreateRequest, user)
        return "redirect:/todos"
    }

    @PostMapping("/{id}/toggle")
    fun toggleComplete(@PathVariable id: Long, session: HttpSession): String {
        val userId = session.getAttribute("userId") as? Long
            ?: return "redirect:/login"

        val user = userService.findById(userId)
        val todo = todoService.getTodoById(id, user)
        todoService.updateTodo(id, TodoUpdateRequest(isDone = !todo.isDone), user)
        return "redirect:/todos"
    }
}

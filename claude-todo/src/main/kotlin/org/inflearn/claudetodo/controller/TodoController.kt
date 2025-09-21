package org.inflearn.claudetodo.controller

import jakarta.servlet.http.HttpSession
import org.inflearn.claudetodo.dto.TodoCreateRequest
import org.inflearn.claudetodo.dto.TodoResponse
import org.inflearn.claudetodo.dto.TodoUpdateRequest
import org.inflearn.claudetodo.service.TodoService
import org.inflearn.claudetodo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/todos")
class TodoController(
    private val todoService: TodoService,
    private val userService: UserService
) {

    @GetMapping
    fun getAllTodos(session: HttpSession): ResponseEntity<List<TodoResponse>> {
        val userId = session.getAttribute("userId") as? Long
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val user = userService.findById(userId)
        val todos = todoService.getAllTodos(user)
        return ResponseEntity.ok(todos)
    }

    @GetMapping("/{id}")
    fun getTodoById(
        @PathVariable id: Long,
        session: HttpSession
    ): ResponseEntity<TodoResponse> {
        val userId = session.getAttribute("userId") as? Long
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        return try {
            val user = userService.findById(userId)
            val todo = todoService.getTodoById(id, user)
            ResponseEntity.ok(todo)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createTodo(
        @RequestBody request: TodoCreateRequest,
        session: HttpSession
    ): ResponseEntity<TodoResponse> {
        val userId = session.getAttribute("userId") as? Long
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val user = userService.findById(userId)
        val createdTodo = todoService.createTodo(request, user)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo)
    }

    @PutMapping("/{id}")
    fun updateTodo(
        @PathVariable id: Long,
        @RequestBody request: TodoUpdateRequest,
        session: HttpSession
    ): ResponseEntity<TodoResponse> {
        val userId = session.getAttribute("userId") as? Long
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        return try {
            val user = userService.findById(userId)
            val updatedTodo = todoService.updateTodo(id, request, user)
            ResponseEntity.ok(updatedTodo)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteTodo(
        @PathVariable id: Long,
        session: HttpSession
    ): ResponseEntity<Void> {
        val userId = session.getAttribute("userId") as? Long
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        return try {
            val user = userService.findById(userId)
            todoService.deleteTodo(id, user)
            ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }
}

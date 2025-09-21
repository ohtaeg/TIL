package org.inflearn.claudetodo.service

import org.inflearn.claudetodo.dto.TodoCreateRequest
import org.inflearn.claudetodo.dto.TodoResponse
import org.inflearn.claudetodo.dto.TodoUpdateRequest
import org.inflearn.claudetodo.entity.Todo
import org.inflearn.claudetodo.entity.User
import org.inflearn.claudetodo.repository.TodoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TodoService(
    private val todoRepository: TodoRepository
) {

    fun getAllTodos(user: User): List<TodoResponse> {
        return todoRepository.findByUser(user)
            .map { TodoResponse.from(it) }
    }

    fun getTodoById(id: Long, user: User): TodoResponse {
        val todo = todoRepository.findByIdAndUser(id, user)
            ?: throw IllegalArgumentException("Todo not found with id: $id")
        return TodoResponse.from(todo)
    }

    @Transactional
    fun createTodo(request: TodoCreateRequest, user: User): TodoResponse {
        val todo = Todo(
            title = request.title,
            description = request.description,
            user = user
        )
        val savedTodo = todoRepository.save(todo)
        return TodoResponse.from(savedTodo)
    }

    @Transactional
    fun updateTodo(id: Long, request: TodoUpdateRequest, user: User): TodoResponse {
        val todo = todoRepository.findByIdAndUser(id, user)
            ?: throw IllegalArgumentException("Todo not found with id: $id")

        request.title?.let { todo.title = it }
        request.description?.let { todo.description = it }
        request.isDone?.let { todo.isDone = it }

        val updatedTodo = todoRepository.save(todo)
        return TodoResponse.from(updatedTodo)
    }

    @Transactional
    fun deleteTodo(id: Long, user: User) {
        val todo = todoRepository.findByIdAndUser(id, user)
            ?: throw IllegalArgumentException("Todo not found with id: $id")
        todoRepository.delete(todo)
    }
}

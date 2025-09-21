package org.inflearn.claudetodo.repository

import org.inflearn.claudetodo.entity.Todo
import org.inflearn.claudetodo.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoRepository : JpaRepository<Todo, Long> {
    fun findByUser(user: User): List<Todo>
    fun findByIdAndUser(id: Long, user: User): Todo?
}

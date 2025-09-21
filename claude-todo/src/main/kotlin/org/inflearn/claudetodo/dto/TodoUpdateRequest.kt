package org.inflearn.claudetodo.dto

data class TodoUpdateRequest(
    val title: String? = null,
    val description: String? = null,
    val isDone: Boolean? = null
)


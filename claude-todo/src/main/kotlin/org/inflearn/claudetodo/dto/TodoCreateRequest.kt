package org.inflearn.claudetodo.dto

data class TodoCreateRequest(
    val title: String,
    val description: String? = null
)


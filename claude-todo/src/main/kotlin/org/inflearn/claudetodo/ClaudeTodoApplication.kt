package org.inflearn.claudetodo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["org.inflearn.claudetodo.repository"])
@EntityScan(basePackages = ["org.inflearn.claudetodo.entity"])
class ClaudeTodoApplication

fun main(args: Array<String>) {
    runApplication<ClaudeTodoApplication>(*args)
}
